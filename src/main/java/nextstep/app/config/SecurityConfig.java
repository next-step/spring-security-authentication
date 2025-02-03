package nextstep.app.config;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.AuthenticationManager;
import nextstep.security.AuthenticationProvider;
import nextstep.security.SecurityFilterChain;
import nextstep.security.UserDetails;
import nextstep.security.UserDetailsService;
import nextstep.security.authentication.DaoAuthenticationProvider;
import nextstep.security.authentication.ProviderManager;
import nextstep.security.basic.BasicAuthenticationConverter;
import nextstep.security.basic.BasicAuthenticationEntrypoint;
import nextstep.security.filter.BasicAuthenticationFilter;
import nextstep.security.filter.DefaultSecurityFilterChain;
import nextstep.security.filter.DelegateFilterProxy;
import nextstep.security.filter.FilterChainProxy;
import nextstep.security.filter.UsernamePasswordAuthenticationFilter;
import nextstep.security.login.UsernamePasswordAuthenticationConverter;
import nextstep.security.login.UsernamePasswordAuthenticationEntrypoint;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {
    private final MemberRepository memberRepository;

    public SecurityConfig(MemberRepository memberRepository) {
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
    public UsernamePasswordAuthenticationConverter usernamePasswordAuthenticationConverter() {
        return new UsernamePasswordAuthenticationConverter();
    }

    @Bean
    public BasicAuthenticationEntrypoint basicAuthenticationEntrypoint() {
        return new BasicAuthenticationEntrypoint();
    }

    @Bean
    public UsernamePasswordAuthenticationEntrypoint usernamePasswordAuthenticationEntrypoint() {
        return new UsernamePasswordAuthenticationEntrypoint();
    }

    @Bean
    public FilterRegistrationBean<DelegateFilterProxy> delegateFilterProxy(FilterChainProxy filterChainProxy) {
        FilterRegistrationBean<DelegateFilterProxy> registrationBean = new FilterRegistrationBean<>();
        DelegateFilterProxy filter = new DelegateFilterProxy(filterChainProxy);

        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @Bean
    public FilterChainProxy filterChainProxy(SecurityFilterChain securityFilterChain) {
        return new FilterChainProxy(
                List.of(securityFilterChain)
        );
    }

    @Bean
    public AuthenticationManager authenticationManager(List<AuthenticationProvider> authenticationProviders) {
        return new ProviderManager(authenticationProviders);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        return new DaoAuthenticationProvider(userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(AuthenticationManager authenticationManager) {
        return new DefaultSecurityFilterChain(
                List.of(
                        new UsernamePasswordAuthenticationFilter(
                                authenticationManager,
                                usernamePasswordAuthenticationConverter(),
                                usernamePasswordAuthenticationEntrypoint()
                        ),

                        new BasicAuthenticationFilter(
                                authenticationManager,
                                basicAuthenticationConverter(),
                                basicAuthenticationEntrypoint()
                        )
                )
        );
    }
}
