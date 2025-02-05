package nextstep.security.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.security.SecurityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HttpSessionSecurityContextRepositoryTest {

    private HttpSessionSecurityContextRepository repository;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private HttpSession mockSession;
    private SecurityContext mockSecurityContext;

    @BeforeEach
    void setUp() {
        repository = new HttpSessionSecurityContextRepository();
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockSession = mock(HttpSession.class);
        mockSecurityContext = mock(SecurityContext.class);
    }

    @Test
    void loadContext_ShouldReturnNull_WhenSessionDoesNotExist() {
        when(mockRequest.getSession(false)).thenReturn(null);

        SecurityContext context = repository.loadContext(mockRequest);

        assertNull(context);
    }

    @Test
    void loadContext_ShouldReturnSecurityContext_WhenSessionExists() {
        when(mockRequest.getSession(false)).thenReturn(mockSession);
        when(mockSession.getAttribute("SPRING_SECURITY_CONTEXT")).thenReturn(mockSecurityContext);

        SecurityContext context = repository.loadContext(mockRequest);

        assertNotNull(context);
        assertSame(mockSecurityContext, context);
    }

    @Test
    void saveContext_ShouldStoreSecurityContextInSession() {
        when(mockRequest.getSession(true)).thenReturn(mockSession);

        repository.saveContext(mockSecurityContext, mockRequest, mockResponse);

        verify(mockSession).setAttribute("SPRING_SECURITY_CONTEXT", mockSecurityContext);
    }
}
