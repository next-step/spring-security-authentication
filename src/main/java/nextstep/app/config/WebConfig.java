package nextstep.app.config;

import java.util.List;
import nextstep.app.service.UserDetailService;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.AuthenticationProvider;
import nextstep.security.authentication.DaoAuthenticationProvider;
import nextstep.security.authentication.ProviderManager;
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
        return new ProviderManager(authenticationProviders());
    }

    @Bean
    public List<AuthenticationProvider> authenticationProviders() {
        return List.of(new DaoAuthenticationProvider(userDetailService));
    }

}
