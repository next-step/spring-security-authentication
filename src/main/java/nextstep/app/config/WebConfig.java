package nextstep.app.config;

import nextstep.app.domain.MemberRepository;
import nextstep.security.BasicAuthenticationInterceptor;
import nextstep.security.MemberDetailService;
import nextstep.security.UsernamePasswordAuthenticationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MemberDetailService memberDetailService;

    public WebConfig(MemberDetailService memberDetailService) {
        this.memberDetailService = memberDetailService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UsernamePasswordAuthenticationInterceptor(memberDetailService)).addPathPatterns("/login");
        registry.addInterceptor(new BasicAuthenticationInterceptor(memberDetailService)).addPathPatterns("/members");
    }
}
