package nextstep.security.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.context.HttpSessionSecurityContextRepository;
import nextstep.security.context.SecurityContextRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static nextstep.security.MockFactory.createLoginRequest;
import static nextstep.security.MockFactory.createUserDetailsService;
import static nextstep.security.Steps.testAuthentication;

class FormAuthorizationFilterTest {
    private final SecurityContextRepository repository = HttpSessionSecurityContextRepository.getInstance();
    private Filter filter;

    @BeforeEach
    void setUp() {
        filter = new FormAuthorizationFilter(createUserDetailsService(), "/login");
    }

    @DisplayName("로그인 Request 가 필터를 통해 인증정보로 바뀐다.")
    @Test
    void doFilter() throws ServletException, IOException {
        final HttpServletRequest request = createLoginRequest();
        filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());
        testAuthentication(request);
    }
}
