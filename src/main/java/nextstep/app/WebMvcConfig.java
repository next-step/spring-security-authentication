package nextstep.app;


import nextstep.security.BasicAuthInterceptor;
import nextstep.security.LoginAuthInterceptor;
import nextstep.security.UserDetailService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final UserDetailService userDetailService;

    public WebMvcConfig(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BasicAuthInterceptor(userDetailService)).addPathPatterns("/members");
        registry.addInterceptor(new LoginAuthInterceptor(userDetailService)).addPathPatterns("/login");
    }
}
