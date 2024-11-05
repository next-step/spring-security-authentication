package nextstep.security.config;

import java.util.List;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.DaoAuthenticationProvider;
import nextstep.security.authentication.ProviderManager;
import nextstep.security.authentication.filter.BasicAuthFilter;
import nextstep.security.authentication.filter.FormLoginAuthFilter;
import nextstep.security.userdetail.UserDetailService;
import nextstep.security.util.TokenDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    public SecurityConfig(UserDetailService userDetailService, TokenDecoder tokenDecoder) {
        this.userDetailService = userDetailService;
        this.tokenDecoder = tokenDecoder;
    }

    private final UserDetailService userDetailService;
    private final TokenDecoder tokenDecoder;

    @Bean
    public DelegatingFilterProxy delegatingFilterProxy(
            AuthenticationManager authenticationManager) {
        return new DelegatingFilterProxy(
                filterChainProxy(List.of(securityFilterChain(authenticationManager))));
    }

    @Bean
    public FilterChainProxy filterChainProxy(List<SecurityFilterChain> securityFilterChains) {
        return new FilterChainProxy(securityFilterChains);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(AuthenticationManager authenticationManager) {
        return new DefaultSecurityFilterChain(
                List.of(new FormLoginAuthFilter(authenticationManager),
                        new BasicAuthFilter(tokenDecoder, authenticationManager)));
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(new DaoAuthenticationProvider(userDetailService)));
    }

}
