package nextstep.security.converter;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.exception.BadCredentialsException;
import nextstep.security.util.Base64Convertor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

class BasicAuthenticationConverterTest {

    private BasicAuthenticationConverter converter;

    @BeforeEach
    void setUp() {
        converter = new BasicAuthenticationConverter();
    }

    @Test
    void convert_ShouldReturnNull_WhenAuthorizationHeaderIsMissing() {
        HttpServletRequest mockRequest = new MockHttpServletRequest();
        Authentication authentication = converter.convert(mockRequest);

        assertNull(authentication);
    }

    @Test
    void convert_ShouldReturnNull_WhenAuthorizationHeaderIsNotBasic() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader("Authorization", "Bearer token");

        Authentication authentication = converter.convert(mockRequest);

        assertNull(authentication);
    }

    @Test
    void convert_ShouldReturnAuthentication_WhenValidBasicAuthProvided() {
        String username = "user";
        String password = "pass";
        String encodedCredentials = Base64Convertor.encode((username + ":" + password));

        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader("Authorization", "Basic " + encodedCredentials);

        Authentication authentication = converter.convert(mockRequest);

        assertNotNull(authentication);
        assertInstanceOf(UsernamePasswordAuthenticationToken.class, authentication);
        assertEquals(username, authentication.getPrincipal());
        assertEquals(password, authentication.getCredentials());
    }

    @Test
    void convert_ShouldThrowException_WhenInvalidBasicAuthFormat() {
        String invalidToken = Base64Convertor.encode("invalidFormat");
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader("Authorization", "Basic " + invalidToken);

        assertThrows(BadCredentialsException.class, () -> converter.convert(mockRequest));
    }
}
