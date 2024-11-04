package nextstep.app.configuration;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.ui.AuthenticationException;
import nextstep.security.configuration.DefaultSecurityFilterChain;
import nextstep.security.configuration.DelegateFilterProxy;
import nextstep.security.configuration.FilterChainProxy;
import nextstep.security.configuration.SecurityFilterChain;
import nextstep.security.configuration.filter.BasicAuthenticationFilter;
import nextstep.security.configuration.filter.FormLoginFilter;
import nextstep.security.model.UserDetails;
import nextstep.security.service.UserDetailsService;
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
                formLoginFilter(),
                basicAuthenticationFilter()
        ));
    }

    @Bean
    public FormLoginFilter formLoginFilter() {
        return new FormLoginFilter(userDetailsService());
    }

    @Bean
    public BasicAuthenticationFilter basicAuthenticationFilter() {
        return new BasicAuthenticationFilter(userDetailsService());
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
