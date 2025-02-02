package nextstep.security.config;
import nextstep.security.interceptor.BasicAuthenticationInterceptor;
import nextstep.security.interceptor.LoginInterceptor;
import nextstep.security.interceptor.MemberValidationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final BasicAuthenticationInterceptor basicAuthenticationInterceptor;
    private final MemberValidationInterceptor memberValidationInterceptor;
    private final LoginInterceptor loginInterceptor;

    @Autowired
    public WebMvcConfig(BasicAuthenticationInterceptor basicAuthenticationInterceptor,
                        MemberValidationInterceptor memberValidationInterceptor,
                        LoginInterceptor loginInterceptor) {
        this.basicAuthenticationInterceptor = basicAuthenticationInterceptor;
        this.memberValidationInterceptor = memberValidationInterceptor;
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/login/**");
        registry.addInterceptor(basicAuthenticationInterceptor).order(0).addPathPatterns("/members/**");
        registry.addInterceptor(memberValidationInterceptor).order(1).addPathPatterns("/members/**");
    }
}
