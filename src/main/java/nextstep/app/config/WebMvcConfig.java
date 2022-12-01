package nextstep.app.config;

import nextstep.app.infrastructure.InmemoryMemberRepository;
import nextstep.app.support.FormUsernamePasswordAuthenticationInterceptor;
import nextstep.app.support.MemberUsernamePasswordAuthenticationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new FormUsernamePasswordAuthenticationInterceptor())
            .addPathPatterns("/login");
        registry.addInterceptor(new MemberUsernamePasswordAuthenticationInterceptor(new InmemoryMemberRepository()))
            .addPathPatterns("/members");
    }
}
