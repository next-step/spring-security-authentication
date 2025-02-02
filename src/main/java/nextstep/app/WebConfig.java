package nextstep.app;

import nextstep.app.domain.CustomUserDetailsService;
import nextstep.app.domain.MemberRepository;
import nextstep.security.BasicAuthFilter;
import nextstep.security.FormAuthFilter;
import nextstep.security.UserDetailsService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final MemberRepository memberRepository;

    public WebConfig(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean
    public FilterRegistrationBean basicAuthFilterRegister() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new BasicAuthFilter(userDetailsService()));
        registrationBean.addUrlPatterns("/members");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean usernamePasswordAuthFilterRegister() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new FormAuthFilter(userDetailsService()));
        registrationBean.addUrlPatterns("/login");
        registrationBean.setOrder(2);
        return registrationBean;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(memberRepository);
    }
}
