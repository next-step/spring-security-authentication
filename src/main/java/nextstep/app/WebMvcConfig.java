package nextstep.app;


import nextstep.app.domain.MemberRepository;
import nextstep.security.BasicAuthInterceptor;
import nextstep.security.LoginAuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberRepository memberRepository;

    public WebMvcConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BasicAuthInterceptor(memberRepository)).addPathPatterns("/members");
        registry.addInterceptor(new LoginAuthInterceptor(memberRepository)).addPathPatterns("/login");
    }
}
