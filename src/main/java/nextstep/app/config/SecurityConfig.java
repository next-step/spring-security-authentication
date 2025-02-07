package nextstep.app.config;

import nextstep.security.authentication.*;
import nextstep.security.context.HttpSessionSecurityContextRepository;
import nextstep.security.context.SecurityContextHolderFilter;
import nextstep.security.context.SecurityContextRepository;
import nextstep.security.filter.DefaultSecurityFilterChain;
import nextstep.security.filter.FilterChainProxy;
import nextstep.security.filter.SecurityFilterChain;
import nextstep.security.userdetails.UserDetailsService;
import nextstep.security.util.request.AllPassRequestMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.GenericFilterBean;

import java.util.List;

@Configuration
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    public SecurityConfig(final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
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
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(new DaoAuthenticationProvider(userDetailsService)));
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SecurityFilterChain securityFilterChain() {
        List<GenericFilterBean> filters = List.of(
                new BasicAuthenticationFilter(authenticationManager()),
                new SecurityContextHolderFilter(securityContextRepository()),
                new LoginAuthenticationFilter(authenticationManager())
        );
        return new DefaultSecurityFilterChain(filters, new AllPassRequestMatcher());
    }

}
