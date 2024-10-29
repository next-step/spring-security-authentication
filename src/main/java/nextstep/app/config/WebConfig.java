package nextstep.app.config;

import nextstep.app.domain.MemberRepository;
import nextstep.app.interceptor.BasicAuthenticationInterceptor;
import nextstep.app.interceptor.UsernamePasswordInterceptor;
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
        registry.addInterceptor(new BasicAuthenticationInterceptor(memberRepository))
                .addPathPatterns("/members");

        registry.addInterceptor(new UsernamePasswordInterceptor(memberRepository));
    }
}
