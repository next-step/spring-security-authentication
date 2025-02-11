package nextstep.app;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.ui.BasicAuthenticationInterceptor;
import nextstep.app.ui.FormLoginInterceptor;
import nextstep.security.BasicAuthenticationFilter;
import nextstep.security.DefaultSecurityFilterChain;
import nextstep.security.FilterChainProxy;
import nextstep.security.SecurityFilterChain;
import nextstep.security.UserDetails;
import nextstep.security.UserDetailsService;
import nextstep.security.UsernamePasswordAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final MemberRepository memberRepository;

    public WebConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean
    public DelegatingFilterProxy delegatingFilterProxy() {
        return new DelegatingFilterProxy(filterChainProxy(List.of(securityFilterChain())));
    }

    @Bean
    public FilterChainProxy filterChainProxy(final List<SecurityFilterChain> securityFilterChainList) {
        return new FilterChainProxy(securityFilterChainList);
    }

    @Bean
    public SecurityFilterChain securityFilterChain() {
        return new DefaultSecurityFilterChain(
                List.of(
                        new BasicAuthenticationFilter(userDetailsService()),
                        new UsernamePasswordAuthenticationFilter(userDetailsService())
                )
        );
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
