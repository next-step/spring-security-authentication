package nextstep.app;

import nextstep.security.config.DefaultSecurityFilterChain;
import nextstep.security.config.DelegatingFilterProxy;
import nextstep.security.config.FilterChainProxy;
import nextstep.security.config.SecurityFilterChain;
import nextstep.security.context.SecurityContextHolderFilter;
import nextstep.security.filter.BasicAuthorizationFilter;
import nextstep.security.filter.FormAuthorizationFilter;
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
                new BasicAuthorizationFilter(userDetailsService),
                new FormAuthorizationFilter(userDetailsService)
        ));
        return new DelegatingFilterProxy(new FilterChainProxy(
                List.of(securityFilterChain)
        ));
    }
}
