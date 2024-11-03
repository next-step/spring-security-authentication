package nextstep.app.config;

import nextstep.app.interceptor.BasicAuthInterceptor;
import nextstep.app.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final BasicAuthInterceptor basicAuthInterceptor;

    @Autowired
    public WebConfig(LoginInterceptor loginInterceptor, BasicAuthInterceptor basicAuthInterceptor) {
        this.loginInterceptor = loginInterceptor;
        this.basicAuthInterceptor = basicAuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/login"); // ID/비밀번호 인증에 대한 경로
        registry.addInterceptor(basicAuthInterceptor)
                .addPathPatterns("/members"); // Basic 인증에 대한 경로
    }
}
