package nextstep.app.config;


import nextstep.security.*;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.DaoAuthenticationProvider;
import nextstep.security.filter.config.FilterChainProxy;
import nextstep.security.filter.config.SecurityFilterChain;
import nextstep.security.user.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.List;
import java.util.Objects;

@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = Objects.requireNonNull(userDetailsService);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(
                new DaoAuthenticationProvider(userDetailsService)
        ));
    }

    @Bean
    public DelegatingFilterProxy delegatingFilterProxy(AuthenticationManager authenticationManager) {
        FilterChainGenerator filterChainGenerator = new FilterChainGenerator(authenticationManager);
        List<SecurityFilterChain> securityFilterChains = List.of(filterChainGenerator.generate());
        FilterChainProxy delegate = new FilterChainProxy(securityFilterChains);

        return new DelegatingFilterProxy(delegate);
    }

}
