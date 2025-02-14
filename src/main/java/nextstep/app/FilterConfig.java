package nextstep.app;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.authentication.AuthenticationManager;
import nextstep.authentication.DaoAuthenticationProvider;
import nextstep.authentication.DefaultSecurityFilterChain;
import nextstep.authentication.FilterChainProxy;
import nextstep.authentication.ProviderManager;
import nextstep.authentication.SecurityFilterChain;
import nextstep.authentication.context.HttpSessionSecurityContextRepository;
import nextstep.authentication.filter.BasicAuthenticationFilter;
import nextstep.authentication.filter.FormLoginAuthenticationFilter;
import nextstep.authentication.UserDetails;
import nextstep.authentication.UserDetailsService;
import nextstep.authentication.context.SecurityContextHolderFilter;
import nextstep.authentication.password.PasswordEncoder;
import nextstep.authentication.password.RawPasswordEncoder;
import nextstep.authentication.util.matcher.AnyRequestMatcher;
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
    public DelegatingFilterProxy delegatingFilterProxy(AuthenticationManager authenticationManager) {
        return new DelegatingFilterProxy(filterChainProxy(authenticationManager));
    }

    private FilterChainProxy filterChainProxy(AuthenticationManager authenticationManager) {
        return new FilterChainProxy(securityFilterChain(authenticationManager));
    }

    private SecurityFilterChain securityFilterChain(AuthenticationManager authenticationManager) {
        return new DefaultSecurityFilterChain(AnyRequestMatcher.INSTANCE, List.of(
                new SecurityContextHolderFilter(new HttpSessionSecurityContextRepository()),
                new BasicAuthenticationFilter(authenticationManager),
                new FormLoginAuthenticationFilter(authenticationManager)
        ));
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(new DaoAuthenticationProvider(userDetailsService(), passwordEncoder())));
    }

    private PasswordEncoder passwordEncoder() {
        return new RawPasswordEncoder();
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
