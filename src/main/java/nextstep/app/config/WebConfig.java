package nextstep.app.config;

import nextstep.security.web.authentication.BasicAuthenticationInterceptor;
import nextstep.security.web.authentication.UsernamePasswordAuthenticationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UsernamePasswordAuthenticationInterceptor());
        registry.addInterceptor(new BasicAuthenticationInterceptor()).addPathPatterns("/members");
    }

}
