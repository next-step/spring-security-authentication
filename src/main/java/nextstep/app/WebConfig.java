package nextstep.app;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.ui.BasicAuthenticationInterceptor;
import nextstep.app.ui.FormLoginInterceptor;
import nextstep.security.BasicAuthenticationFilter;
import nextstep.security.UserDetails;
import nextstep.security.UserDetailsService;
import nextstep.security.UsernamePasswordAuthenticationFilter;
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

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new FormLoginInterceptor(userDetailsService())).addPathPatterns("/login");
//        registry.addInterceptor(new BasicAuthenticationInterceptor(userDetailsService())).addPathPatterns("/members");
//    }

    @Bean
    public UserDetailsService userDetailsService() {
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

    @Bean
    public FilterRegistrationBean basicAuthenticationFilterRegister() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new BasicAuthenticationFilter(userDetailsService()));
        registrationBean.addUrlPatterns("/members");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean usernamePasswordAuthenticationFilterRegister() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new UsernamePasswordAuthenticationFilter(userDetailsService()));
        registrationBean.addUrlPatterns("/login");
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
