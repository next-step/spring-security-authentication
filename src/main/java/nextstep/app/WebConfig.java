package nextstep.app;

import nextstep.app.domain.LoginService;
import nextstep.app.domain.MemberRepository;
import nextstep.security.BasicAuthenticationInterceptor;
import nextstep.security.FormLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginService loginService;
    private final MemberRepository memberRepository;

    public WebConfig(LoginService loginService, MemberRepository memberRepository) {
        this.loginService = loginService;
        this.memberRepository = memberRepository;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("WebConfig.addInterceptors");
        registry.addInterceptor(new FormLoginInterceptor(loginService))
                .addPathPatterns("/login");
        registry.addInterceptor(new BasicAuthenticationInterceptor(loginService, memberRepository))
                .addPathPatterns("/members");
    }
}
