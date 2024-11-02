package nextstep.app.config;

import lombok.RequiredArgsConstructor;
import nextstep.security.constants.SecurityConstants;
import nextstep.security.interceptor.BasicAuthInterceptor;
import nextstep.security.interceptor.UsernamePasswordInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final UsernamePasswordInterceptor usernamePasswordInterceptor;
    private final BasicAuthInterceptor basicAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(usernamePasswordInterceptor).addPathPatterns(SecurityConstants.LOGIN_URL);
        registry.addInterceptor(basicAuthInterceptor).excludePathPatterns(SecurityConstants.LOGIN_URL);

    }
}
