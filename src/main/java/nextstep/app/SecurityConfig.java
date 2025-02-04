package nextstep.app;

import nextstep.security.DefaultSecurityFilterChain;
import nextstep.security.FilterChainProxy;
import nextstep.security.UserDetailsService;
import nextstep.security.filter.BasicAuthenticationFilter;
import nextstep.security.filter.FormLoginAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public DelegatingFilterProxy delegatingFilterProxy() {
        DefaultSecurityFilterChain securityFilterChains = new DefaultSecurityFilterChain(
                List.of(new BasicAuthenticationFilter(userDetailsService)
                      , new FormLoginAuthenticationFilter(userDetailsService))
        );

        FilterChainProxy filterChainProxy = new FilterChainProxy(List.of(securityFilterChains));

        return new DelegatingFilterProxy(filterChainProxy);
    }
}
