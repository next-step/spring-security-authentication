package nextstep.app.config;

import lombok.RequiredArgsConstructor;
import nextstep.security.core.DefaultSecurityFilterChain;
import nextstep.security.core.authentication.AuthenticationManager;
import nextstep.security.core.FilterChainProxy;
import nextstep.security.core.authentication.provider.AbstractUserDetailsAuthenticationProvider;
import nextstep.security.core.userdetails.UserDetailService;
import nextstep.security.filter.BasicTokenAuthenticationFilter;
import nextstep.security.filter.UsernamePasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SpringSecurityConfig {
    private final UserDetailService userDetailService;
    private AuthenticationManager authenticationManager;

    @Bean
    public DelegatingFilterProxy delegatingFilterProxy() {
        final List<Filter> filters = List.of(
                new UsernamePasswordAuthenticationFilter(authenticationManager()),
                new BasicTokenAuthenticationFilter(authenticationManager())
        );

        FilterChainProxy filterChainProxy = new FilterChainProxy(List.of(new DefaultSecurityFilterChain(filters)));

        return new DelegatingFilterProxy(filterChainProxy);
    }

    private AuthenticationManager authenticationManager() {
        if (this.authenticationManager == null) {
            authenticationManager = new AuthenticationManager(List.of(usernamePasswordAuthenticationProvider()));
        }
        return this.authenticationManager;
    }

    private AbstractUserDetailsAuthenticationProvider usernamePasswordAuthenticationProvider() {
        return new AbstractUserDetailsAuthenticationProvider(userDetailService);
    }

}
