package nextstep.app.config;

import nextstep.security.interceptor.BasicAuthenticationInterceptor;
import nextstep.security.interceptor.FormLoginAuthorizationInterceptor;
import nextstep.security.service.UserDetailService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final UserDetailService userDetailService;

    public WebConfig(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new FormLoginAuthorizationInterceptor(userDetailService)).addPathPatterns("/login");
        registry.addInterceptor(new BasicAuthenticationInterceptor(userDetailService)).addPathPatterns("/members");
    }
}
