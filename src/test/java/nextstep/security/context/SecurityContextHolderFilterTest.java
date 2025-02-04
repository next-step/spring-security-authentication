package nextstep.security.context;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static nextstep.security.MockFactory.PASSWORD;
import static nextstep.security.MockFactory.USERNAME;
import static nextstep.security.MockFactory.createFilterChain;
import static nextstep.security.MockFactory.createSecurityContext;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SecurityContextHolderFilterTest {
    private final SecurityContextRepository repository = HttpSessionSecurityContextRepository.getInstance();
    private SecurityContextHolderFilter filter;

    @BeforeEach
    void setUp() {
        filter = new SecurityContextHolderFilter();
    }


    @DisplayName("SecurityContextHolder 가 clear 되더라도, session 에는 SecurityContext 가 남아있다.")
    @Test
    void doFilter() throws ServletException, IOException {
        // given
        final HttpServletRequest request = createLoginRequest();
        final SecurityContext oldContext = createSecurityContext(
                "My Name", "My Password"
        );
        SecurityContextHolder.setContext(oldContext);
        assertAll(
                () -> assertThat(SecurityContextHolder.getContext())
                        .isEqualTo(oldContext),
                () -> assertThat(repository.loadContext(request))
                        .isEqualTo(SecurityContext.empty())
        );

        // when
        filter.doFilter(request, new MockHttpServletResponse(), createFilterChain());

        // then
        assertAll(
                () -> assertThat(SecurityContextHolder.getContext())
                        .isEqualTo(SecurityContext.empty()),
                () -> assertThat(repository.loadContext(request))
                        .isNotEqualTo(oldContext)
        );
    }

    private HttpServletRequest createLoginRequest() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/login");
        request.setParameter("username", USERNAME);
        request.setParameter("password", PASSWORD);
        request.setContentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        return request;
    }
}
