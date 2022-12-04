package nextstep.app.configuration;

import nextstep.app.interceptor.BasicAuthenticationInterceptor;
import nextstep.app.interceptor.LoginAuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private BasicAuthenticationInterceptor basicAuthenticationInterceptor;

    @Autowired
    private LoginAuthenticationInterceptor loginAuthenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(basicAuthenticationInterceptor).excludePathPatterns("/login");
        registry.addInterceptor(loginAuthenticationInterceptor).addPathPatterns("/login");
    }
}
