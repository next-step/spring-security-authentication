package nextstep.app.config;

import nextstep.security.DefaultSecurityFilterChain;
import nextstep.security.filter.AuthorizationFilter;
import nextstep.security.filter.BasicAuthenticationFilter;
import nextstep.security.filter.FilterChainProxy;
import nextstep.security.filter.FormLoginAuthFilter;
import nextstep.security.manager.ProviderManager;
import nextstep.security.provider.UsernamePasswordProvider;
import nextstep.security.service.UserDetailsService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.GenericFilterBean;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public GenericFilterBean delegatingFilterProxy() {
        return new FilterChainProxy(List.of(
                new DefaultSecurityFilterChain("/login", List.of(
                        new FormLoginAuthFilter(new ProviderManager(List.of(new UsernamePasswordProvider(userDetailsService)))),
                        new AuthorizationFilter()
                )),
                new DefaultSecurityFilterChain("/members", List.of(
                        new BasicAuthenticationFilter(new ProviderManager(List.of(new UsernamePasswordProvider(userDetailsService)))),
                        new AuthorizationFilter()
                ))
        ));
    }

    @Bean
    public FilterRegistrationBean<GenericFilterBean> delegatingFilterProxyFilterRegistrationBean(GenericFilterBean delegatingFilterProxy) {
        FilterRegistrationBean<GenericFilterBean> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(delegatingFilterProxy);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
