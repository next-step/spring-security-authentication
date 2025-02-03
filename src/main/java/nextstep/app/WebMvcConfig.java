package nextstep.app;


import nextstep.security.BasicAuthFilter;
import nextstep.security.LoginAuthInterceptor;
import nextstep.security.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final UserDetailService userDetailService;

    public WebMvcConfig(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginAuthInterceptor(userDetailService)).addPathPatterns("/login");
    }

    @Bean
    public DelegatingFilterProxy delegatingFilterProxy() {
        return new DelegatingFilterProxy(new BasicAuthFilter(userDetailService));
    }

}
