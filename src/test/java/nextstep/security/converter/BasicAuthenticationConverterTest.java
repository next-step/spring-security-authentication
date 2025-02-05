package nextstep.security.converter;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.exception.BadCredentialsException;
import nextstep.security.util.Base64Convertor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BasicAuthenticationConverterTest {

    private BasicAuthenticationConverter converter;
    private HttpServletRequest mockRequest;

    @BeforeEach
    void setUp() {
        converter = new BasicAuthenticationConverter();
        mockRequest = mock(HttpServletRequest.class);
    }

    @Test
    void convert_ShouldReturnNull_WhenAuthorizationHeaderIsMissing() {
        when(mockRequest.getHeader("Authorization")).thenReturn(null);

        Authentication authentication = converter.convert(mockRequest);

        assertNull(authentication);
    }

    @Test
    void convert_ShouldReturnNull_WhenAuthorizationHeaderIsNotBasic() {
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer token");

        Authentication authentication = converter.convert(mockRequest);

        assertNull(authentication);
    }

    @Test
    void convert_ShouldReturnAuthentication_WhenValidBasicAuthProvided() {
        String username = "user";
        String password = "pass";
        String encodedCredentials = Base64Convertor.encode((username + ":" + password));

        when(mockRequest.getHeader("Authorization")).thenReturn("Basic " + encodedCredentials);

        Authentication authentication = converter.convert(mockRequest);

        assertNotNull(authentication);
        assertInstanceOf(UsernamePasswordAuthenticationToken.class, authentication);
        assertEquals(username, authentication.getPrincipal());
        assertEquals(password, authentication.getCredentials());
    }

    @Test
    void convert_ShouldThrowException_WhenInvalidBasicAuthFormat() {
        String invalidToken = Base64Convertor.encode("invalidFormat");
        when(mockRequest.getHeader("Authorization")).thenReturn("Basic " + invalidToken);

        assertThrows(BadCredentialsException.class, () -> converter.convert(mockRequest));
    }
}
