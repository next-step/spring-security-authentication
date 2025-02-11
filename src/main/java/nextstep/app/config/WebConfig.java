package nextstep.app.config;

import jakarta.servlet.Filter;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.UserDetailService;
import nextstep.security.UserDetails;
import nextstep.security.config.AuthenticationManager;
import nextstep.security.config.AuthenticationProvider;
import nextstep.security.config.BasicAuthenticationProvider;
import nextstep.security.config.DaoAuthenticationProvider;
import nextstep.security.config.DefaultSecurityFilterChain;
import nextstep.security.config.FilterChainProxy;
import nextstep.security.config.ProviderManager;
import nextstep.security.config.SecurityContextHolderFilter;
import nextstep.security.config.SecurityFilterChain;
import nextstep.security.filter.BasicAuthFilter;
import nextstep.security.filter.UsernamePasswordAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
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
        return new DelegatingFilterProxy(filterChainProxy());
    }

    @Bean
    public FilterChainProxy filterChainProxy() {
        List<SecurityFilterChain> securityFilterChains = List.of(securityFilterChain());
        return new FilterChainProxy(securityFilterChains);
    }

    @Bean
    public SecurityFilterChain securityFilterChain() {
        List<Filter> securityFilters = List.of(
                new SecurityContextHolderFilter(),
                new BasicAuthFilter(authenticationManager()),
                new UsernamePasswordAuthFilter(authenticationManager())
        );
        return new DefaultSecurityFilterChain(securityFilters);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> providers = List.of(
                daoAuthenticationProvider(),
                basicAuthenticationProvider()
        );
        return new ProviderManager(providers);
    }

    @Bean
    public BasicAuthenticationProvider basicAuthenticationProvider() {
        return new BasicAuthenticationProvider(userDetailService());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        return new DaoAuthenticationProvider(userDetailService());
    }

    @Bean
    public UserDetailService userDetailService() {
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
