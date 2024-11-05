package nextstep.app.configuration;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.ui.AuthenticationException;
import nextstep.security.configuration.filter.BasicAuthenticationFilter;
import nextstep.security.configuration.filter.FormLoginFilter;
import nextstep.security.configuration.filter.SecurityContextHolderFilter;
import nextstep.security.model.UserDetails;
import nextstep.security.service.UserDetailsService;
import nextstep.security.service.filter.DefaultSecurityFilterChain;
import nextstep.security.service.filter.DelegateFilterProxy;
import nextstep.security.service.filter.FilterChainProxy;
import nextstep.security.service.filter.SecurityFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final MemberRepository memberRepository;

    @Bean
    public DelegateFilterProxy delegateFilterProxy() {
        return new DelegateFilterProxy(filterChainProxy(List.of(securityFilterChain())));
    }

    @Bean
    public FilterChainProxy filterChainProxy(List<SecurityFilterChain> securityFilterChains) {
        return new FilterChainProxy(securityFilterChains);
    }

    @Bean
    public SecurityFilterChain securityFilterChain() {
        return new DefaultSecurityFilterChain(List.of(
                new SecurityContextHolderFilter(),
                new FormLoginFilter(userDetailsService()),
                new BasicAuthenticationFilter(userDetailsService())
        ));
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public Optional<UserDetails> loadUserByUsername(String username) {
                Member member = memberRepository.findByEmail(username).orElseThrow(AuthenticationException::new);
                return Optional.ofNullable(
                        UserDetails.builder().userName(member.getEmail()).password(member.getPassword()).build());
            }

            @Override
            public Optional<UserDetails> loadUserByUsernameAndEmail(String username, String password) {
                Member member =
                        memberRepository.findByEmailAndPassword(username, password)
                                .orElseThrow(AuthenticationException::new);
                return Optional.ofNullable(
                        UserDetails.builder().userName(member.getEmail()).password(member.getPassword()).build());
            }
        };
    }


}
