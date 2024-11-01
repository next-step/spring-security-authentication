package nextstep.config;

import nextstep.security.BasicAuthenticationInterceptor;
import nextstep.security.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor());
        registry.addInterceptor(new BasicAuthenticationInterceptor()).addPathPatterns("/members");
    }
}
