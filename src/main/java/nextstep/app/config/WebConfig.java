package nextstep.app.config;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.filter.BasicAuthFilter;
import nextstep.security.filter.UsernamePasswordAuthFilter;
import nextstep.security.interceptor.BasicAuthInterceptor;
import nextstep.security.interceptor.FormLoginInterceptor;
import nextstep.security.UserDetailService;
import nextstep.security.UserDetails;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
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
        registry.addInterceptor(new FormLoginInterceptor(userDetailService())).addPathPatterns("/login");
        registry.addInterceptor(new BasicAuthInterceptor(userDetailService())).addPathPatterns("/members");
    }

    @Bean
    public FilterRegistrationBean<UsernamePasswordAuthFilter> usernamePasswordAuthenticationFilterRegister() {
        FilterRegistrationBean<UsernamePasswordAuthFilter> registrationBean = new FilterRegistrationBean<>(new UsernamePasswordAuthFilter(userDetailService()));
        registrationBean.addUrlPatterns("/login");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<BasicAuthFilter> basicAuthenticationFilterRegister() {
        FilterRegistrationBean<BasicAuthFilter> registrationBean = new FilterRegistrationBean<>(new BasicAuthFilter(userDetailService()));
        registrationBean.addUrlPatterns("/members");
        registrationBean.setOrder(2);
        return registrationBean;
    }

    @Bean
    public UserDetailService userDetailService() {
        return username -> {
            Member member = memberRepository.findByEmail(username)
                    .orElseThrow(() -> new IllegalArgumentException("해당하는 사용자를 찾을 수 없습니다."));
            return new UserDetails() {
                @Override
                public String getUsername() {
                    return member.getEmail();
                }

                @Override
                public String getPassword() {
                    return member.getPassword();
                }
            };
        };
    }

}
