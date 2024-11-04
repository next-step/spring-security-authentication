package nextstep.app.config;

import java.util.List;
import nextstep.security.filter.DefaultSecurityFilterChain;
import nextstep.security.filter.DelegatingFilterProxy;
import nextstep.security.filter.FilterChainProxy;
import nextstep.security.filter.SecurityFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain() {
        return new DefaultSecurityFilterChain(
                // todo 우리가 만든 필터를 여기에 추가해야 함.
                List.of()
        );
    }

    @Bean
    public FilterChainProxy filterChainProxy(List<SecurityFilterChain> securityFilterChains) {
        // 여러 개의 시큐리티 필터 체인을 목록으로 가진다.
        return new FilterChainProxy(securityFilterChains);
    }

    @Bean
    public DelegatingFilterProxy delegatingFilterProxy() {
        // 필터 체인 프록시에게 위임하는 역할을 한다.
        return new DelegatingFilterProxy(filterChainProxy(List.of(securityFilterChain())));
    }

}
