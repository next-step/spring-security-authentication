package nextstep.security.context;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.jupiter.api.Assertions.*;

class HttpSessionSecurityContextRepositoryTest {

    private HttpSessionSecurityContextRepository repository;

    @BeforeEach
    void setUp() {
        repository = new HttpSessionSecurityContextRepository();
    }

    @Test
    void loadContext_ShouldReturnNull_WhenSessionDoesNotExist() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        SecurityContext context = repository.loadContext(mockRequest);

        assertNull(context);
    }

    @Test
    void loadContext_ShouldReturnSecurityContext_WhenSessionExists() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpSession mockSession = new MockHttpSession();
        SecurityContext mockSecurityContext = new SecurityContextImpl();
        mockSession.setAttribute("SPRING_SECURITY_CONTEXT", mockSecurityContext);
        mockRequest.setSession(mockSession);

        SecurityContext context = repository.loadContext(mockRequest);

        assertNotNull(context);
        assertSame(mockSecurityContext, context);
    }

    @Test
    void saveContext_ShouldStoreSecurityContextInSession() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpSession mockSession = new MockHttpSession();
        SecurityContext mockSecurityContext = new SecurityContextImpl();
        mockRequest.setSession(mockSession);

        repository.saveContext(mockSecurityContext, mockRequest, new MockHttpServletResponse());

        assertSame(mockSecurityContext, mockSession.getAttribute("SPRING_SECURITY_CONTEXT"));
    }
}
