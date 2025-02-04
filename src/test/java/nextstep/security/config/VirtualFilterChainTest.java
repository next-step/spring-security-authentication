package nextstep.security.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.MockFactory;
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

import static nextstep.security.MockFactory.createBasicRequest;
import static nextstep.security.MockFactory.createLoginRequest;
import static nextstep.security.Steps.testAuthentication;

class VirtualFilterChainTest {
    private VirtualFilterChain filterChain;

    @BeforeEach
    void setUp() {
        final UserDetailsService userDetailsService = MockFactory.createUserDetailsService();
        filterChain = new VirtualFilterChain(new MockFilterChain(), List.of(
                new BasicAuthorizationFilter(userDetailsService),
                new FormAuthorizationFilter(userDetailsService, "/login")
        ));
    }

    @DisplayName("BasicAuthorizationFilter 의 동작을 확인")
    @Test
    void basicAuth() throws ServletException, IOException {
        final HttpServletRequest request = createBasicRequest();
        filterChain.doFilter(request, new MockHttpServletResponse());
        testAuthentication(request);
    }

    @DisplayName("FormAuthorizationFilter 의 동작을 확인")
    @Test
    void formAuth() throws ServletException, IOException {
        final HttpServletRequest request = createLoginRequest();
        filterChain.doFilter(request, new MockHttpServletResponse());
        testAuthentication(request);
    }
}
