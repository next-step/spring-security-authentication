package nextstep.app.config;

import nextstep.app.support.FormUsernamePasswordAuthenticationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new FormUsernamePasswordAuthenticationInterceptor())
            .addPathPatterns("/login");
    }
}
