package nextstep.app;

import nextstep.security.filter.BasicAuthorizationFilter;
import nextstep.security.user.UserDetailsService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public FilterRegistrationBean basicAuthentication(
            UserDetailsService userDetailsService
    ) {
        FilterRegistrationBean registration = new FilterRegistrationBean(
                new BasicAuthorizationFilter(userDetailsService)
        );
        registration.addUrlPatterns("/members");
        registration.setOrder(2);
        return registration;
    }
}
