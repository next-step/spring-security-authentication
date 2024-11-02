package nextstep.app.config;

import lombok.RequiredArgsConstructor;
import nextstep.app.constants.AppConstants;
import nextstep.app.interceptor.BasicAuthInterceptor;
import nextstep.app.interceptor.UsernamePasswordInterceptor;
import nextstep.app.service.MemberService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final MemberService memberService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UsernamePasswordInterceptor(memberService)).addPathPatterns(AppConstants.LOGIN_URL);
        registry.addInterceptor(new BasicAuthInterceptor(memberService)).excludePathPatterns(AppConstants.LOGIN_URL);

    }
}
