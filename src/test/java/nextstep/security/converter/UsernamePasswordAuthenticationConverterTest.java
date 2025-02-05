package nextstep.security.converter;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UsernamePasswordAuthenticationConverterTest {

    private UsernamePasswordAuthenticationConverter converter;
    private HttpServletRequest mockRequest;

    @BeforeEach
    void setUp() {
        converter = new UsernamePasswordAuthenticationConverter();
        mockRequest = mock(HttpServletRequest.class);
    }

    @Test
    void convert_ShouldReturnAuthentication_WithValidUsernameAndPassword() {
        when(mockRequest.getParameter("username")).thenReturn("user");
        when(mockRequest.getParameter("password")).thenReturn("pass");

        Authentication authentication = converter.convert(mockRequest);

        assertNotNull(authentication);
        assertInstanceOf(UsernamePasswordAuthenticationToken.class, authentication);
        assertEquals("user", authentication.getPrincipal());
        assertEquals("pass", authentication.getCredentials());
    }

    @Test
    void convert_ShouldReturnAuthentication_WithEmptyStrings_WhenParametersAreNull() {
        when(mockRequest.getParameter("username")).thenReturn(null);
        when(mockRequest.getParameter("password")).thenReturn(null);

        Authentication authentication = converter.convert(mockRequest);

        assertNotNull(authentication);
        assertInstanceOf(UsernamePasswordAuthenticationToken.class, authentication);
        assertEquals("", authentication.getPrincipal());
        assertEquals("", authentication.getCredentials());
    }
}
