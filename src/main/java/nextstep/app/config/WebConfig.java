package nextstep.app.config;

import nextstep.security.interceptor.BasicAuthenticationInterceptor;
import nextstep.security.interceptor.FormLoginAuthenticationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final FormLoginAuthenticationInterceptor formLoginAuthenticationInterceptor;
    private final BasicAuthenticationInterceptor basicAuthenticationInterceptor;

    public WebConfig(FormLoginAuthenticationInterceptor formLoginAuthenticationInterceptor,
                     BasicAuthenticationInterceptor basicAuthenticationInterceptor) {
        this.formLoginAuthenticationInterceptor = formLoginAuthenticationInterceptor;
        this.basicAuthenticationInterceptor = basicAuthenticationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(basicAuthenticationInterceptor)
                .addPathPatterns("/members");

        registry.addInterceptor(formLoginAuthenticationInterceptor);
    }
}
