package nextstep.app;


import nextstep.security.SecurityContextRepository;
import nextstep.security.UserNamePasswordSecurityFilterChain;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.filter.*;
import nextstep.security.user.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.List;

@Configuration
public class SecurityConfig {

    private static final String[] BASIC_AUTH_PATH = new String[]{"/members"};
    private static final String[] USER_NAME_PASSWORD_AUTH_PATH = new String[]{"/login"};

    private final UserDetailsService userDetailsService;
    private final SecurityContextRepository securityContextRepository;

    public SecurityConfig(UserDetailsService userDetailsService, SecurityContextRepository securityContextRepository) {
        this.userDetailsService = userDetailsService;
        this.securityContextRepository = securityContextRepository;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(
                new DaoAuthenticationProvider(userDetailsService)
        ));
    }

    @Bean
    public DelegatingFilterProxy delegatingFilterProxy() {
        List<SecurityFilterChain> securityFilterChains = List.of(basicSecurityFilterChain(), userNamePasswordSecurityFilterChain());
        FilterChainProxy delegate = new FilterChainProxy(securityFilterChains);
        return new DelegatingFilterProxy(delegate);
    }

    @Bean
    public SecurityFilterChain basicSecurityFilterChain() {
        return new BasicSecurityFilterChain(
                BASIC_AUTH_PATH,
                List.of(
                        new BasicAuthFilter(authenticationManager(), securityContextRepository),
                        new SecurityContextHolderFilter(securityContextRepository)
                )
        );
    }

    @Bean
    public SecurityFilterChain userNamePasswordSecurityFilterChain() {
        return new UserNamePasswordSecurityFilterChain(
                USER_NAME_PASSWORD_AUTH_PATH,
                List.of(new UserNamePasswordAuthFilter(authenticationManager(), securityContextRepository))
        );
    }
}
