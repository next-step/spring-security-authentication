package nextstep.app.config;

import nextstep.security.interceptor.AuthorizationInterceptor;
import nextstep.security.interceptor.BasicAuthenticationInterceptor;
import nextstep.security.interceptor.FormLoginAuthInterceptor;
import nextstep.security.service.UserDetailsService;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final UserDetailsService userDetailsService;

    public WebConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BasicAuthenticationInterceptor(userDetailsService));
        registry.addInterceptor(new FormLoginAuthInterceptor(userDetailsService)).addPathPatterns("/login");
        registry.addInterceptor(new AuthorizationInterceptor()).addPathPatterns("/members");
    }
}
