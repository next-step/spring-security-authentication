package nextstep.app.config;

import nextstep.security.SecurityFilterChain;
import nextstep.security.filter.AuthorizationFilter;
import nextstep.security.filter.BasicAuthenticationFilter;
import nextstep.security.filter.FilterChainProxy;
import nextstep.security.filter.FormLoginAuthFilter;
import nextstep.security.service.UserDetailsService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
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
                new SecurityFilterChain() {
                    @Override
                    public boolean matches(HttpServletRequest request) {
                        return request.getRequestURI().equals("/login");
                    }

                    @Override
                    public List<Filter> getFilters() {
                        return List.of(
                                new FormLoginAuthFilter(userDetailsService),
                                new AuthorizationFilter()
                        );
                    }
                },
                new SecurityFilterChain() {
                    @Override
                    public boolean matches(HttpServletRequest request) {
                        return request.getRequestURI().startsWith("/members");
                    }

                    @Override
                    public List<Filter> getFilters() {
                        return List.of(
                                new BasicAuthenticationFilter(userDetailsService),
                                new AuthorizationFilter()
                        );
                    }
                }
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
