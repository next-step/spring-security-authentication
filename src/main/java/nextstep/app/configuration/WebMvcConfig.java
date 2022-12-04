package nextstep.app.configuration;

import nextstep.security.authentication.BasicAuthenticationInterceptor;
import nextstep.security.authentication.FormAuthenticationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class WebMvcConfig implements WebMvcConfigurer {

    private final BasicAuthenticationInterceptor basicAuthenticationInterceptor;
    private final FormAuthenticationInterceptor formAuthenticationInterceptor;

    WebMvcConfig(BasicAuthenticationInterceptor basicAuthenticationInterceptor, FormAuthenticationInterceptor formAuthenticationInterceptor) {
        this.basicAuthenticationInterceptor = basicAuthenticationInterceptor;
        this.formAuthenticationInterceptor = formAuthenticationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(basicAuthenticationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login");
        registry.addInterceptor(formAuthenticationInterceptor)
                .addPathPatterns("/login");
    }
}
