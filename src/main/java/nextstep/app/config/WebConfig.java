package nextstep.app.config;

import nextstep.app.service.MemberService;
import nextstep.security.interceptor.BasicAuthInterceptor;
import nextstep.security.interceptor.IdPasswordAuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final MemberService memberService;

    public WebConfig(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new IdPasswordAuthInterceptor(memberService))
                .addPathPatterns("/login");
        registry.addInterceptor(new BasicAuthInterceptor(memberService))
                .addPathPatterns("/members");
    }


}
