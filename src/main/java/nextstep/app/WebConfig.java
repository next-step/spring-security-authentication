package nextstep.app;

import nextstep.security.web.authentication.BasicAuthenticationInterceptor;
import nextstep.security.web.authentication.UsernamePasswordAuthenticationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UsernamePasswordAuthenticationInterceptor()).addPathPatterns("/login");
        registry.addInterceptor(new BasicAuthenticationInterceptor()).addPathPatterns("/members");
        //해당 경로에 인가 인터셉터 추가
    }

}
