package nextstep.app;

import nextstep.app.domain.CustomUserDetailsService;
import nextstep.app.domain.MemberRepository;
import nextstep.security.authentication.Role;
import nextstep.security.core.context.HttpSessionSecurityContextRepository;
import nextstep.security.core.context.SecurityContextRepository;
import nextstep.security.filter.AuthorizationFilter;
import nextstep.security.filter.SecurityContextHolderFilter;
import nextstep.security.filter.config.DefaultSecurityFilterChain;
import nextstep.security.filter.config.DelegatingFilterProxy;
import nextstep.security.filter.config.FilterChainProxy;
import nextstep.security.filter.config.SecurityFilterChain;
import nextstep.security.core.uesrdetails.UserDetailsService;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.AuthenticationProvider;
import nextstep.security.authentication.DaoAuthenticationProvider;
import nextstep.security.authentication.ProviderManager;
import nextstep.security.filter.BasicAuthFilter;
import nextstep.security.filter.FormAuthFilter;
import nextstep.security.util.PasswordMatcher;
import nextstep.security.util.PlainTextPasswordMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class SecurityConfig {
    private final MemberRepository memberRepository;

    public SecurityConfig(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
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
                List.of(
                        new SecurityContextHolderFilter(securityContextRepository()),
                        new FormAuthFilter(authenticationManager(), securityContextRepository()),
                        new BasicAuthFilter(authenticationManager(), securityContextRepository()),
                        new AuthorizationFilter(Map.of("/members", List.of(Role.NORMAL))
                        )
                )
        );
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> providers = List.of(daoAuthenticationProvider());
        return new ProviderManager(providers);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordMatcher(), userDetailsService());
        return provider;
    }

    @Bean
    public PasswordMatcher passwordMatcher() {
        return new PlainTextPasswordMatcher();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(memberRepository);
    }
}
