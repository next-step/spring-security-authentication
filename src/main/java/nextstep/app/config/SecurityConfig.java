package nextstep.app.config;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.filter.BasicAuthenticationFilter;
import nextstep.security.filter.FormLoginFilter;
import nextstep.security.filter.SecurityContextHolderFilter;
import nextstep.security.filterchain.DefaultFilterChain;
import nextstep.security.filterchain.SecurityFilterChain;
import nextstep.security.proxy.DelegatingFilterProxy;
import nextstep.security.proxy.FilterChainProxy;
import nextstep.security.user.UserDetails;
import nextstep.security.user.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SecurityConfig {
    private final MemberRepository memberRepository;

    public SecurityConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean
    public DelegatingFilterProxy delegatingFilterProxy() {
        return new DelegatingFilterProxy(filterChainProxy(List.of(securityFilterChain())));
    }

    @Bean
    public FilterChainProxy filterChainProxy(List<SecurityFilterChain> securityFilterChains) {
        return new FilterChainProxy(securityFilterChains);
    }

    @Bean
    public SecurityFilterChain securityFilterChain() {
        return new DefaultFilterChain(List.of(new SecurityContextHolderFilter()
                , new FormLoginFilter(userDetailsService())
                , new BasicAuthenticationFilter(userDetailsService())));
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Member member = memberRepository.findByEmail(username)
                    .orElseThrow(() -> new IllegalArgumentException("해당하는 사용자를 찾을 수 없습니다."));
            return new UserDetails() {
                @Override
                public String getUsername() {
                    return member.getEmail();
                }

                @Override
                public String getPassword() {
                    return member.getPassword();
                }
            };
        };
    }
}
