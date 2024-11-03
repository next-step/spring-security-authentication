package nextstep.app.config;

import nextstep.security.authentication.BasicAuthInterceptor;
import nextstep.security.authentication.FormLoginAuthInterceptor;
import nextstep.security.authentication.TokenDecoder;
import nextstep.security.userdetail.UserDetailService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final UserDetailService userDetailService;
    private final TokenDecoder tokenDecoder;

    public WebConfig(UserDetailService userDetailService, TokenDecoder tokenDecoder) {
        this.userDetailService = userDetailService;
        this.tokenDecoder = tokenDecoder;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new FormLoginAuthInterceptor(userDetailService))
                .addPathPatterns("/login");
        registry.addInterceptor(new BasicAuthInterceptor(tokenDecoder, userDetailService))
                .addPathPatterns("/members");
    }
}
