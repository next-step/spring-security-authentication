package nextstep.app.ui;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.BasicAuthenticationInterceptor;
import nextstep.security.authentication.DaoAuthenticationProvider;
import nextstep.security.authentication.FormLoginAuthenticationInterceptor;
import nextstep.security.authentication.ProviderManager;
import nextstep.security.filter.BasicAuthenticationSecurityFilter;
import nextstep.security.filter.DefaultSecurityFilterChain;
import nextstep.security.filter.FormLoginAuthenticationFilter;
import nextstep.security.filter.SecurityFilterChain;
import nextstep.security.userdetils.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final UserDetailsService userDetailsService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new FormLoginAuthenticationInterceptor(authenticationManager())).addPathPatterns("/login");
        registry.addInterceptor(new BasicAuthenticationInterceptor(authenticationManager()));
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(daoAuthenticationProvider()));
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        return new DaoAuthenticationProvider(userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(AuthenticationManager authenticationManager){
        return new DefaultSecurityFilterChain(
                List.of(new FormLoginAuthenticationFilter(authenticationManager),
                        new BasicAuthenticationSecurityFilter(authenticationManager))
        );
    }
}
