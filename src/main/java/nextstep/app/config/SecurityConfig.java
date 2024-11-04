package nextstep.app.config;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.ui.AuthenticationException;
import nextstep.security.DefaultSecurityFilterChain;
import nextstep.security.DelegatingFilterProxy;
import nextstep.security.FilterChainProxy;
import nextstep.security.SecurityFilterChain;
import nextstep.security.filter.BasicAuthenticationFilter;
import nextstep.security.filter.UsernamePasswordAuthenticationFilter;
import nextstep.security.model.UserDetail;
import nextstep.security.service.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SecurityConfig {
    private final MemberRepository memberRepository;

    public SecurityConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Bean
    public DelegatingFilterProxy delegatingFilterProxy() {
        return new DelegatingFilterProxy(filterChainProxy(List.of(securityFilterChain())));
    }
    @Bean
    public FilterChainProxy filterChainProxy(List<SecurityFilterChain> securityFilterChains) {
        return new FilterChainProxy(securityFilterChains);
    }

    @Bean
    public SecurityFilterChain securityFilterChain() {
        return new DefaultSecurityFilterChain(List.of(
                new BasicAuthenticationFilter(userDetailService()),
                new UsernamePasswordAuthenticationFilter(userDetailService())
        ));
    }
    @Bean
    public UserDetailService userDetailService() {
        return username -> {
            Member member = memberRepository.findByEmail(username)
                    .orElseThrow(() -> new AuthenticationException("존재하지 않는 사용자입니다."));

            return new UserDetail() {
                @Override
                public String getUserName() {
                    return member.getUserName();
                }

                @Override
                public String getPassword() {
                    return member.getPassword();
                }
            };
        };
    }
}
