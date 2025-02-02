package nextstep.app;

import nextstep.app.domain.MemberRepository;
import nextstep.app.interceptor.BasicAuthInterceptor;
import nextstep.app.interceptor.FormAuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final MemberRepository memberRepository;

    public WebConfig(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new FormAuthInterceptor(memberRepository)).addPathPatterns("/login");
        registry.addInterceptor(new BasicAuthInterceptor(memberRepository)).addPathPatterns("/members");
    }
}
