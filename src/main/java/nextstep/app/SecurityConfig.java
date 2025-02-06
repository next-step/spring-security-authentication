package nextstep.app;

import nextstep.security.config.DefaultSecurityFilterChain;
import nextstep.security.config.DelegatingFilterProxy;
import nextstep.security.config.FilterChainProxy;
import nextstep.security.config.SecurityFilterChain;
import nextstep.security.context.SecurityContextHolderFilter;
import nextstep.security.filter.BasicAuthenticationFilter;
import nextstep.security.filter.FormAuthenticationFilter;
import nextstep.security.user.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SecurityConfig {
    @Bean
    public DelegatingFilterProxy delegatingFilterProxy(
            UserDetailsService userDetailsService
    ) {
        final SecurityFilterChain securityFilterChain = new DefaultSecurityFilterChain(List.of(
                new SecurityContextHolderFilter(),
                new BasicAuthenticationFilter(userDetailsService),
                new FormAuthenticationFilter(userDetailsService, "/login")
        ));
        return new DelegatingFilterProxy(new FilterChainProxy(
                List.of(securityFilterChain)
        ));
    }
}
