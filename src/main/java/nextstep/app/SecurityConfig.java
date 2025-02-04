package nextstep.app;

import nextstep.security.*;
import nextstep.security.filter.BasicAuthenticationFilter;
import nextstep.security.filter.FormLoginAuthenticationFilter;
import nextstep.security.provider.DaoAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.List;
import java.util.Set;

@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Set.of(new DaoAuthenticationProvider(userDetailsService)));
    }

    @Bean
    public DelegatingFilterProxy delegatingFilterProxy(AuthenticationManager authenticationManager) {
        DefaultSecurityFilterChain securityFilterChains = new DefaultSecurityFilterChain(
                List.of(new BasicAuthenticationFilter(authenticationManager)
                      , new FormLoginAuthenticationFilter(authenticationManager))
        );

        FilterChainProxy filterChainProxy = new FilterChainProxy(List.of(securityFilterChains));

        return new DelegatingFilterProxy(filterChainProxy);
    }
}
