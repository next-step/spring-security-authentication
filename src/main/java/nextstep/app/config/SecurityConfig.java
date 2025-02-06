package nextstep.app.config;

import nextstep.app.domain.MemberRepository;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.AuthenticationProvider;
import nextstep.security.authentication.BasicAuthenticationFailureHandler;
import nextstep.security.authentication.BasicAuthenticationSuccessHandler;
import nextstep.security.authentication.UsernamePasswordAuthenticationFailureHandler;
import nextstep.security.authentication.UsernamePasswordAuthenticationSuccessHandler;
import nextstep.security.context.SecurityContextRepository;
import nextstep.security.filter.SecurityFilterChain;
import nextstep.security.userdetails.UserDetailsService;
import nextstep.security.authentication.DaoAuthenticationProvider;
import nextstep.security.authentication.ProviderManager;
import nextstep.security.context.DelegatingSecurityContextRepository;
import nextstep.security.context.HttpSessionSecurityContextRepository;
import nextstep.security.converter.BasicAuthenticationConverter;
import nextstep.security.converter.UsernamePasswordAuthenticationConverter;
import nextstep.security.filter.BasicAuthenticationFilter;
import nextstep.security.filter.DefaultSecurityFilterChain;
import nextstep.security.filter.DelegatingFilterProxy;
import nextstep.security.filter.FilterChainProxy;
import nextstep.security.filter.SecurityContextHolderFilter;
import nextstep.security.filter.UsernamePasswordAuthenticationFilter;
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
        return username -> memberRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 사용자를 찾을 수 없습니다."));
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
    public FilterRegistrationBean<DelegatingFilterProxy> delegateFilterProxy(SecurityFilterChain securityFilterChain) {
        FilterRegistrationBean<DelegatingFilterProxy> registrationBean = new FilterRegistrationBean<>();
        DelegatingFilterProxy filter = new DelegatingFilterProxy(
                new FilterChainProxy(
                        List.of(securityFilterChain)
                )
        );

        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
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
    public SecurityFilterChain securityFilterChain(AuthenticationManager authenticationManager,
                                                   SecurityContextRepository securityContextRepository) {
        return new DefaultSecurityFilterChain(
                List.of(
                        new SecurityContextHolderFilter(
                                securityContextRepository
                        ),
                        new UsernamePasswordAuthenticationFilter(
                                authenticationManager,
                                usernamePasswordAuthenticationConverter(),
                                new UsernamePasswordAuthenticationSuccessHandler(securityContextRepository),
                                new UsernamePasswordAuthenticationFailureHandler()
                        ),
                        new BasicAuthenticationFilter(
                                authenticationManager,
                                basicAuthenticationConverter(),
                                new BasicAuthenticationSuccessHandler(securityContextRepository),
                                new BasicAuthenticationFailureHandler()
                        )
                )
        );
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(new HttpSessionSecurityContextRepository());
    }
}
