package nextstep.app.config;

import nextstep.app.domain.UserDetailServiceImpl;
import nextstep.security.BasicAuthentication;
import nextstep.security.FormAuthenticationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final UserDetailServiceImpl userDetailService;

    public WebConfig(UserDetailServiceImpl userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new FormAuthenticationInterceptor(userDetailService)).addPathPatterns("/login");
        registry.addInterceptor(new BasicAuthentication(userDetailService)).addPathPatterns("/members");
    }
}
