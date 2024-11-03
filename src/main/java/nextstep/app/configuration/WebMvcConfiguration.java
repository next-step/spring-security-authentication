package nextstep.app.configuration;

import lombok.RequiredArgsConstructor;
import nextstep.app.configuration.interceptor.BasicAuthenticationInterceptor;
import nextstep.app.configuration.interceptor.MemberAuthorizationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final MemberAuthorizationInterceptor memberAuthorizationInterceptor;
    private final BasicAuthenticationInterceptor basicAuthenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(memberAuthorizationInterceptor).addPathPatterns("/login");
        registry.addInterceptor(basicAuthenticationInterceptor).addPathPatterns("/members");
    }
}
