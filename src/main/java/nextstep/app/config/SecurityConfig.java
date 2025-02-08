package nextstep.app.config;

import nextstep.app.domain.Base64PasswordEncoder;
import nextstep.security.authentication.provider.AuthenticationManager;
import nextstep.security.authentication.provider.AuthenticationProvider;
import nextstep.security.authentication.provider.DaoAuthenticationProvider;
import nextstep.security.authentication.provider.ProviderManager;
import nextstep.security.filter.BasicAuthenticationFilter;
import nextstep.security.filter.FormLoginAuthenticationFilter;
import nextstep.security.filter.SecurityContextHolderFilter;
import nextstep.security.user.UserDetailsService;
import nextstep.security.web.DefaultSecurityFilterChain;
import nextstep.security.web.DelegatingFilterProxy;
import nextstep.security.web.FilterChainProxy;
import nextstep.security.web.SecurityFilterChain;
import nextstep.security.web.matcher.AnyRequestMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SecurityConfig{

    private final UserDetailsService userDetailsService;
    private final Base64PasswordEncoder passwordEncoder;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = new Base64PasswordEncoder();
    }

    @Bean
    public DelegatingFilterProxy delegatingFilterProxy() {
        SecurityFilterChain securityFilterChain = new DefaultSecurityFilterChain(
                AnyRequestMatcher.INSTANCE,
                List.of(
                        new BasicAuthenticationFilter(authenticationManager()),
                        new FormLoginAuthenticationFilter(authenticationManager()),
                        new SecurityContextHolderFilter(authenticationManager())
                )
        );

        List<SecurityFilterChain> filterChains = List.of(securityFilterChain);
        return new DelegatingFilterProxy(new FilterChainProxy(filterChains));
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> providers = List.of(daoAuthenticationProvider());
        return new ProviderManager(providers);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        return new DaoAuthenticationProvider(userDetailsService, passwordEncoder);
    }

}
