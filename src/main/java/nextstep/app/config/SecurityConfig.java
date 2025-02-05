package nextstep.app.config;

import nextstep.security.AuthenticationManager;
import nextstep.security.AuthenticationProvider;
import nextstep.security.DaoAuthenticationProvider;
import nextstep.security.ProviderManager;
import nextstep.security.domain.MemberDetailService;
import nextstep.security.filter.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final MemberDetailService memberDetailService;

    public SecurityConfig(MemberDetailService memberDetailService) {
        this.memberDetailService = memberDetailService;
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
    public SecurityFilterChain securityFilterChain() {
        return new DefaultSecurityFilterChain(
                List.of(new ExceptionHandlerFilter(),
                        new BasicAuthenticationFilter(authenticationManager()),
                        new LoginAuthenticationFilter(authenticationManager()))
        );
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> providers = List.of(daoAuthenticationProvider());
        return new ProviderManager(providers);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        return new DaoAuthenticationProvider(memberDetailService);
    }
}
