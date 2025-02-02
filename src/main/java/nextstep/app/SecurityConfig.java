package nextstep.app;

import nextstep.app.domain.CustomUserDetailsService;
import nextstep.app.domain.MemberRepository;
import nextstep.security.filter.BasicAuthFilter;
import nextstep.security.DefaultSecurityFilterChain;
import nextstep.security.DelegatingFilterProxy;
import nextstep.security.FilterChainProxy;
import nextstep.security.filter.FormAuthFilter;
import nextstep.security.SecurityFilterChain;
import nextstep.security.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SecurityConfig {
    private final MemberRepository memberRepository;

    public SecurityConfig(final MemberRepository memberRepository) {
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
        return new DefaultSecurityFilterChain(
                List.of(
                        new FormAuthFilter(userDetailsService()),
                        new BasicAuthFilter(userDetailsService())
                )
        );
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(memberRepository);
    }
}
