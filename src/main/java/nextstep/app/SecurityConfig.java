package nextstep.app;


import nextstep.security.BasicAuthFilter;
import nextstep.security.LoginAuthFilter;
import nextstep.security.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

@Configuration
public class SecurityConfig {

    private final UserDetailService userDetailService;

    public SecurityConfig(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Bean
    public DelegatingFilterProxy basicAuthFilterProxy() {
        return new DelegatingFilterProxy(new BasicAuthFilter(userDetailService));
    }

    @Bean
    public DelegatingFilterProxy loginAuthFilterProxy() {
        return new DelegatingFilterProxy(new LoginAuthFilter(userDetailService));
    }
}
