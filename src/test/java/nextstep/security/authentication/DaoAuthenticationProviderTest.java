package nextstep.security.authentication;

import nextstep.app.domain.Member;
import nextstep.security.exception.BadCredentialsException;
import nextstep.security.userdetails.UserDetails;
import nextstep.security.userdetails.UserDetailsService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DaoAuthenticationProviderTest {

    UserDetails user1 = new Member("a.a@gmail.com", "valid", "name", "imageUrl");
    UserDetailsService userDetailsService = username -> {
        if (user1.getUsername().equals(username)) {
            return user1;
        }

        throw new IllegalStateException("User not found");
    };

    @Test
    void authenticateValidUser() {
        // given
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        Authentication authRequest = new UsernamePasswordAuthenticationToken(
                user1.getUsername(), user1.getPassword());

        // when
        Authentication authResult = daoAuthenticationProvider.authenticate(authRequest);

        // then
        assertTrue(authResult.isAuthenticated());
    }

    @Test
    void authenticateInvalidUser() {
        // given
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        Authentication authRequest = new UsernamePasswordAuthenticationToken(
                user1.getUsername(), "invalid");

        // expect
        assertThrows(BadCredentialsException.class,
                () -> daoAuthenticationProvider.authenticate(authRequest));
    }
}
