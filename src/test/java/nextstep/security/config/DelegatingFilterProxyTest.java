package nextstep.security.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.Fixture;
import nextstep.security.filter.BasicAuthorizationFilter;
import nextstep.security.filter.FormAuthorizationFilter;
import nextstep.security.user.UserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.List;

import static nextstep.security.Fixture.createBasicRequest;
import static nextstep.security.Fixture.createLoginRequest;
import static nextstep.security.Steps.testAuthentication;

class DelegatingFilterProxyTest {
    private DelegatingFilterProxy filter;

    @BeforeEach
    void setUp() {
        final UserDetailsService userDetailsService = Fixture.createUserDetailsService();
        final SecurityFilterChain filterChain = new DefaultSecurityFilterChain(List.of(
                new BasicAuthorizationFilter(userDetailsService),
                new FormAuthorizationFilter(userDetailsService, "/login")
        ));
        filter = new DelegatingFilterProxy(new FilterChainProxy(List.of(filterChain)));
    }

    @DisplayName("BasicAuthorizationFilter 의 동작을 확인")
    @Test
    void basicAuth() throws ServletException, IOException {
        final HttpServletRequest request = createBasicRequest();
        filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());
        testAuthentication(request);
    }

    @DisplayName("FormAuthorizationFilter 의 동작을 확인")
    @Test
    void formAuth() throws ServletException, IOException {
        final HttpServletRequest request = createLoginRequest();
        filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());
        testAuthentication(request);
    }

}
