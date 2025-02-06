package nextstep.app;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.DefaultSecurityFilterChain;
import nextstep.security.FilterChainProxy;
import nextstep.security.SecurityFilterChain;
import nextstep.security.filter.BasicAuthenticationFilter;
import nextstep.security.filter.FormLoginAuthenticationFilter;
import nextstep.security.UserDetails;
import nextstep.security.UserDetailsService;
import nextstep.security.util.matcher.AnyRequestMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class FilterConfig implements WebMvcConfigurer {

    private final MemberRepository memberRepository;

    public FilterConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean
    public DelegatingFilterProxy delegatingFilterProxy() {
        return new DelegatingFilterProxy(filterChainProxy());
    }

    private FilterChainProxy filterChainProxy() {
        return new FilterChainProxy(securityFilterChain());
    }

    private SecurityFilterChain securityFilterChain() {
        return new DefaultSecurityFilterChain(AnyRequestMatcher.INSTANCE, List.of(
                new BasicAuthenticationFilter(userDetailsService()),
                new FormLoginAuthenticationFilter(userDetailsService())
        ));
    }

    private UserDetailsService userDetailsService() {
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
