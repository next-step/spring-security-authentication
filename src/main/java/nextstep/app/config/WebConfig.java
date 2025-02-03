package nextstep.app.config;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.UserDetails;
import nextstep.security.UserDetailsService;
import nextstep.security.basic.BasicAuthenticationConverter;
import nextstep.security.basic.BasicAuthenticationEntrypoint;
import nextstep.security.filter.BasicAuthenticationFilter;
import nextstep.security.filter.UsernamePasswordAuthenticationFilter;
import nextstep.security.login.UsernamePasswordAuthenticationConverter;
import nextstep.security.login.UsernamePasswordAuthenticationEntrypoint;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final MemberRepository memberRepository;

    public WebConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
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
    public BasicAuthenticationConverter basicAuthenticationConverter() {
        return new BasicAuthenticationConverter();
    }

    @Bean
    public UsernamePasswordAuthenticationConverter formLoginAuthenticationConverter() {
        return new UsernamePasswordAuthenticationConverter();
    }

    @Bean
    public BasicAuthenticationEntrypoint basicAuthenticationEntrypoint() {
        return new BasicAuthenticationEntrypoint();
    }

    @Bean
    public UsernamePasswordAuthenticationEntrypoint formLoginAuthenticationEntrypoint() {
        return new UsernamePasswordAuthenticationEntrypoint();
    }

    @Bean
    public FilterRegistrationBean<BasicAuthenticationFilter> basicAuthenticationFilter() {
        FilterRegistrationBean<BasicAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        BasicAuthenticationFilter filter = new BasicAuthenticationFilter(
                userDetailsService(),
                basicAuthenticationConverter(),
                basicAuthenticationEntrypoint()
        );
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/members");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<UsernamePasswordAuthenticationFilter> formLoginAuthenticationFilter() {
        FilterRegistrationBean<UsernamePasswordAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter(
                userDetailsService(),
                formLoginAuthenticationConverter(),
                formLoginAuthenticationEntrypoint()
        );
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/login");
        return registrationBean;
    }
}
