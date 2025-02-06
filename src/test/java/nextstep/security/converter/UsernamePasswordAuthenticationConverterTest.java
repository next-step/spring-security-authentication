package nextstep.security.converter;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

class UsernamePasswordAuthenticationConverterTest {

    private UsernamePasswordAuthenticationConverter converter;

    @BeforeEach
    void setUp() {
        converter = new UsernamePasswordAuthenticationConverter();
    }

    @Test
    void convert_ShouldReturnAuthentication_WithValidUsernameAndPassword() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setParameter("username", "user");
        mockRequest.setParameter("password", "pass");

        Authentication authentication = converter.convert(mockRequest);

        assertNotNull(authentication);
        assertInstanceOf(UsernamePasswordAuthenticationToken.class, authentication);
        assertEquals("user", authentication.getPrincipal());
        assertEquals("pass", authentication.getCredentials());
    }

    @Test
    void convert_ShouldReturnAuthentication_WithEmptyStrings_WhenParametersAreNull() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();

        Authentication authentication = converter.convert(mockRequest);

        assertNotNull(authentication);
        assertInstanceOf(UsernamePasswordAuthenticationToken.class, authentication);
        assertEquals("", authentication.getPrincipal());
        assertEquals("", authentication.getCredentials());
    }
}
