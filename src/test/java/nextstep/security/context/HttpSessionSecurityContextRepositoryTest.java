package nextstep.security.context;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import static org.assertj.core.api.Assertions.assertThat;

class HttpSessionSecurityContextRepositoryTest {
    private HttpSessionSecurityContextRepository httpSessionSecurityContextRepository;
    private MockHttpServletRequest request;
    private MockHttpSession session;
    private final String securityContextKey = "securityContext";

    @BeforeEach
    void setUp() {
        httpSessionSecurityContextRepository = new HttpSessionSecurityContextRepository(securityContextKey);
        request = new MockHttpServletRequest();
        session = new MockHttpSession();
    }

    @Test
    @DisplayName("세션이 존재하지 않은 경우 인증이 비어있는 컨텍스트를 가져온다")
    void no_session_lode_context() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(null); // 세션이 없는 상태

        SecurityContext context = httpSessionSecurityContextRepository.loadContext(request);

        assertThat(context.getAuthentication()).isNull();
    }


    @Test
    @DisplayName("세션이 존재하는 경우 세션의 컨텍스트를 가져온다")
    void session_load_context () {
        SecurityContext sessionContext = new DefaultSecurityContext();

        session.setAttribute(securityContextKey, sessionContext);
        request.setSession(session);

        SecurityContext loadContext = httpSessionSecurityContextRepository.loadContext(request);

        assertThat(loadContext).isEqualTo(sessionContext);
    }

    @Test
    @DisplayName("SecurityContext 에 올바르게 저장된다.")
    void load_session_context() {
        MockHttpSession session = new MockHttpSession();
        SecurityContext context = new DefaultSecurityContext();
        request.setSession(session);

        httpSessionSecurityContextRepository.saveContext(context, request, new MockHttpServletResponse());

        assertThat(context).isEqualTo(SecurityContextHolder.getContext());
    }
}
