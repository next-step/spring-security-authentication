package nextstep.app.config;

import nextstep.app.interceptor.BasicAuthInterceptor;
import nextstep.app.interceptor.IdPasswordAuthInterceptor;
import nextstep.app.domain.MemberRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final MemberRepository memberRepository;

    public WebConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new IdPasswordAuthInterceptor(memberRepository))
                .addPathPatterns("/login");
        registry.addInterceptor(new BasicAuthInterceptor(memberRepository))
                .addPathPatterns("/members");
    }
}
