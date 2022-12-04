package nextstep.app.config;

import nextstep.app.infrastructure.InmemoryMemberRepository;
import nextstep.security.authentication.FormAuthenticationInterceptor;
import nextstep.security.authentication.HttpBasicAuthenticationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new FormAuthenticationInterceptor())
            .addPathPatterns("/login");
        registry.addInterceptor(new HttpBasicAuthenticationInterceptor(new InmemoryMemberRepository()))
            .addPathPatterns("/members");
    }
}
