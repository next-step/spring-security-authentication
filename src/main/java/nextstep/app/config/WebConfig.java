package nextstep.app.config;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.AuthenticationConverter;
import nextstep.security.AuthenticationEntrypoint;
import nextstep.security.basic.BasicAuthenticationConverter;
import nextstep.security.basic.BasicAuthenticationEntrypoint;
import nextstep.security.filter.BasicAuthenticationFilter;
import nextstep.security.FormLoginInterceptor;
import nextstep.security.UserDetails;
import nextstep.security.UserDetailsService;
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
        registry.addInterceptor(new FormLoginInterceptor(userDetailsService())).addPathPatterns("/login");
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

    @Bean
    public AuthenticationConverter authenticationConverter() {
        return new BasicAuthenticationConverter();
    }

    @Bean
    public AuthenticationEntrypoint authenticationEntrypoint() {
        return new BasicAuthenticationEntrypoint();
    }

    @Bean
    public FilterRegistrationBean<BasicAuthenticationFilter> basicAuthenticationFilter() {
        FilterRegistrationBean<BasicAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        BasicAuthenticationFilter filter = new BasicAuthenticationFilter(
                userDetailsService(),
                authenticationConverter(),
                authenticationEntrypoint()
        );
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/members");
        return registrationBean;
    }
}
