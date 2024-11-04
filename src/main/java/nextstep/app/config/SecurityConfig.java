package nextstep.app.config;

import nextstep.security.DefaultSecurityFilterChain;
import nextstep.security.DelegatingFilterProxy;
import nextstep.security.FilterChainProxy;
import nextstep.security.SecurityFilterChain;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class SecurityConfig {

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
        return new DefaultSecurityFilterChain(List.of());
    }
}
