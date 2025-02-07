package nextstep.security.config;

import jakarta.servlet.Filter;
import nextstep.security.UserDetailService;
import nextstep.security.filter.BasicAuthFilter;
import nextstep.security.filter.UsernamePasswordAuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final UserDetailService userDetailsService;

    public SecurityConfig(UserDetailService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public FilterRegistrationBean<DelegatingFilterProxy> delegatingFilterProxyFilterRegister() {
        FilterRegistrationBean<DelegatingFilterProxy> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new DelegatingFilterProxy("filterChainProxy"));
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

    @Bean
    public DelegatingFilterProxy delegatingFilterProxy() {
        return new DelegatingFilterProxy(filterChainProxy());
    }

    @Bean
    public FilterChainProxy filterChainProxy() {
        List<SecurityFilterChain> securityFilterChains = List.of(securityFilterChain());
        return new FilterChainProxy(securityFilterChains);
    }

    @Bean
    public SecurityFilterChain securityFilterChain() {
        List<Filter> securityFilters = List.of(
                new BasicAuthFilter(userDetailsService),
                new UsernamePasswordAuthFilter(userDetailsService)
        );
        return new DefaultSecurityFilterChain(securityFilters);
    }

}
