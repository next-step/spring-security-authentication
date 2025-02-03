package nextstep.app;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.filter.BasicAuthenticationFilter;
import nextstep.security.filter.FormLoginAuthenticationFilter;
import nextstep.security.UserDetails;
import nextstep.security.UserDetailsService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FilterConfig implements WebMvcConfigurer {

    private final MemberRepository memberRepository;

    public FilterConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean
    public FilterRegistrationBean<BasicAuthenticationFilter> basicAuthenticationFilterBean() {
        FilterRegistrationBean<BasicAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new BasicAuthenticationFilter(userDetailsService()));
        registrationBean.addUrlPatterns("/members");

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<FormLoginAuthenticationFilter> formLoginAuthenticationFilterBean() {
        FilterRegistrationBean<FormLoginAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new FormLoginAuthenticationFilter(userDetailsService()));
        registrationBean.addUrlPatterns("/login");

        return registrationBean;
    }

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
}
