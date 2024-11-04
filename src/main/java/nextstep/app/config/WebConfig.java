package nextstep.app.config;

import java.util.List;
import nextstep.app.service.UserDetailService;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.AuthenticationProvider;
import nextstep.security.authentication.DefaultAuthenticationManager;
import nextstep.security.authentication.DefaultAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final UserDetailService userDetailService;

    public WebConfig(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new DefaultAuthenticationManager(authenticationProviders());
    }

    @Bean
    public List<AuthenticationProvider> authenticationProviders() {
        return List.of(new DefaultAuthenticationProvider(userDetailService));
    }

}
