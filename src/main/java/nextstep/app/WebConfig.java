package nextstep.app;

import nextstep.app.domain.CustomUserDetailsService;
import nextstep.app.domain.MemberRepository;
import nextstep.security.BasicAuthInterceptor;
import nextstep.security.FormAuthInterceptor;
import nextstep.security.UserDetailsService;
import org.springframework.context.annotation.Bean;
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
        registry.addInterceptor(new FormAuthInterceptor(userDetailsService())).addPathPatterns("/login");
        registry.addInterceptor(new BasicAuthInterceptor(userDetailsService())).addPathPatterns("/members");
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(memberRepository);
    }
}
