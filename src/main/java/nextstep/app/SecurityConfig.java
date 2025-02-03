package nextstep.app;


import nextstep.security.BasicAuthFilter;
import nextstep.security.LoginAuthFilter;
import nextstep.security.UserDetailsService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    private final UserDetailService userDetailService;

    public SecurityConfig(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Bean
    public FilterRegistrationBean<BasicAuthFilter> basicAuthFilterRegister() {
        FilterRegistrationBean<BasicAuthFilter> registrationBean = new FilterRegistrationBean<>(new BasicAuthFilter(userDetailsService));
        registrationBean.addUrlPatterns(BASIC_AUTH_PATH);
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<LoginAuthFilter> loginAuthFilterRegister() {
        FilterRegistrationBean<LoginAuthFilter> registrationBean = new FilterRegistrationBean<>(new LoginAuthFilter(userDetailsService));
        registrationBean.addUrlPatterns(LOGIN_AUTH_PATH);
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
