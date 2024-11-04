package nextstep.app.configuration;

import lombok.RequiredArgsConstructor;
import nextstep.app.configuration.interceptor.BasicAuthenticationInterceptor;
import nextstep.app.configuration.interceptor.FormLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final FormLoginInterceptor formLoginInterceptor;
    private final BasicAuthenticationInterceptor basicAuthenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(formLoginInterceptor).addPathPatterns("/login");
        registry.addInterceptor(basicAuthenticationInterceptor).addPathPatterns("/members");
    }
}
