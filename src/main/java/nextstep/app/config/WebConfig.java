package nextstep.app.config;

import nextstep.security.authentication.FormLoginAuthInterceptor;
import nextstep.security.userdetail.UserDetailService;
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
        registry.addInterceptor(new FormLoginAuthInterceptor(userDetailService))
                .addPathPatterns("/login");
    }
}
