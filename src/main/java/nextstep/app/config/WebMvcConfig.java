package nextstep.app.config;

import nextstep.app.applicaion.MemberService;
import nextstep.security.authentication.AuthenticationProvider;
import nextstep.security.authentication.FormAuthenticationInterceptor;
import nextstep.security.authentication.HttpBasicAuthenticationInterceptor;
import nextstep.security.authentication.UsernamePasswordAuthenticationProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberService memberService;

    public WebMvcConfig(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        final AuthenticationProvider provider = new UsernamePasswordAuthenticationProvider(memberService);
        registry.addInterceptor(new FormAuthenticationInterceptor(provider))
            .addPathPatterns("/login");
        registry.addInterceptor(new HttpBasicAuthenticationInterceptor(provider))
            .addPathPatterns("/members");
    }
}
