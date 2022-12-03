package nextstep.app.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class WebMvcConfig implements WebMvcConfigurer {

    private final BasicAuthenticationInterceptor basicAuthenticationInterceptor;

    public WebMvcConfig(BasicAuthenticationInterceptor basicAuthenticationInterceptor) {
        this.basicAuthenticationInterceptor = basicAuthenticationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(basicAuthenticationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login");
    }
}
