package nextstep.security.authentication.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import nextstep.util.AuthenticationFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.security.authentication.context.SessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SecurityContextRepositoryTest {

    private SecurityContextRepository securityContextRepository = SessionSecurityContextRepository.getInstance();
    private HttpServletRequest request;
    private HttpSession session;
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);
        this.securityContext = SecurityContextImpl.from(AuthenticationFixture.createAuthentication());

        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, securityContext);
    }


    @DisplayName("세션이 없으면 null로 반환한다.")
    @Test
    void loadContext() {
        when(request.getSession(false)).thenReturn(null);

        SecurityContext securityContext = securityContextRepository.loadContext(request);
        assertThat(securityContext).isNull();
    }

    @DisplayName("세션이 있으면 그 값을 반환한다.")
    @Test
    void isExistsSession() {
        when(request.getSession(false)).thenReturn(session);

        SecurityContext securityContext = securityContextRepository.loadContext(request);
        assertThat(securityContext).isEqualTo(securityContext);
    }

}
