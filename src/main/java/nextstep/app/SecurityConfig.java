package nextstep.app;


import nextstep.security.filter.BasicAuthFilter;
import nextstep.security.filter.LoginAuthFilter;
import nextstep.security.UserDetailsService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private static final String[] BASIC_AUTH_PATH = new String[]{"/members"};
    private static final String[] LOGIN_AUTH_PATH = new String[] { "/login" };

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
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
