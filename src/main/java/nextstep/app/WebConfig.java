package nextstep.app;

import nextstep.security.BasicAuthenticationFilter;
import nextstep.security.FormLoginAuthenticationFilter;
import nextstep.security.UserDetailsService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final UserDetailsService userDetailsService;

    public WebConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public FilterRegistrationBean<BasicAuthenticationFilter> basicAuthenticationFilter() {
        FilterRegistrationBean<BasicAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new BasicAuthenticationFilter(userDetailsService));
        registrationBean.addUrlPatterns("/members");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<FormLoginAuthenticationFilter> formLoginAuthenticationFilter() {
        FilterRegistrationBean<FormLoginAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new FormLoginAuthenticationFilter(userDetailsService));
        registrationBean.addUrlPatterns("/login");
        return registrationBean;
    }
}
