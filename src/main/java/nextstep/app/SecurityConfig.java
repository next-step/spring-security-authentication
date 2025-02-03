package nextstep.app;


import nextstep.security.AuthenticationManager;
import nextstep.security.UserDetailsService;
import nextstep.security.filter.BasicAuthFilter;
import nextstep.security.filter.LoginAuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    private static final String[] BASIC_AUTH_PATH = new String[]{"/members"};
    private static final String[] LOGIN_AUTH_PATH = new String[]{"/login"};

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(
                new DaoAuthenticationProvider(userDetailsService)
        ));
    }

    @Bean
    public FilterRegistrationBean<BasicAuthFilter> basicAuthFilterRegister() {
        BasicAuthFilter basicAuthFilter = new BasicAuthFilter(authenticationManager());
        FilterRegistrationBean<BasicAuthFilter> registrationBean = new FilterRegistrationBean<>(basicAuthFilter);
        registrationBean.addUrlPatterns(BASIC_AUTH_PATH);
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<LoginAuthFilter> loginAuthFilterRegister() {
        LoginAuthFilter loginAuthFilter = new LoginAuthFilter(authenticationManager());
        FilterRegistrationBean<LoginAuthFilter> registrationBean = new FilterRegistrationBean<>(loginAuthFilter);
        registrationBean.addUrlPatterns(LOGIN_AUTH_PATH);
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
