package nextstep.app;

import nextstep.security.DefaultSecurityFilterChain;
import nextstep.security.FilterChainProxy;
import nextstep.security.UserDetailsService;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.DaoAuthenticationProvider;
import nextstep.security.authentication.ProviderManager;
import nextstep.security.context.HttpSessionSecurityContextRepository;
import nextstep.security.context.SecurityContextRepository;
import nextstep.security.filter.BasicAuthenticationFilter;
import nextstep.security.filter.SecurityContextHolderFilter;
import nextstep.security.filter.UsernamePasswordAuthenticationFilter;
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
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public DefaultSecurityFilterChain securityFilterChain(SecurityContextRepository securityContextRepository) {
        AuthenticationManager providerManager = new ProviderManager(Set.of(new DaoAuthenticationProvider(userDetailsService)));

        return new DefaultSecurityFilterChain(
                List.of(new SecurityContextHolderFilter(securityContextRepository)
                        , new UsernamePasswordAuthenticationFilter(providerManager)
                        , new BasicAuthenticationFilter(providerManager))
        );
    }

    @Bean
    public DelegatingFilterProxy delegatingFilterProxy(DefaultSecurityFilterChain defaultSecurityFilterChain) {
        return new DelegatingFilterProxy(new FilterChainProxy(List.of(defaultSecurityFilterChain)));
    }
}
