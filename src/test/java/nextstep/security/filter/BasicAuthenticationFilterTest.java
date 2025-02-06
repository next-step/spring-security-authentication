package nextstep.security.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static nextstep.security.Fixture.createBasicRequest;
import static nextstep.security.Fixture.createUserDetailsService;
import static nextstep.security.Steps.testAuthentication;

class BasicAuthenticationFilterTest {
    private Filter filter;

    @BeforeEach
    void setUp() {
        filter = new BasicAuthenticationFilter(createUserDetailsService());
    }

    @DisplayName("Basic Authorization 이 필터를 통해 인증될 수 있다.")
    @Test
    void doFilter() throws ServletException, IOException {
        final HttpServletRequest request = createBasicRequest();
        filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());
        testAuthentication(request);
    }
}
