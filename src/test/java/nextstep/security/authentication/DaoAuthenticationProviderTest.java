package nextstep.security.authentication;

import nextstep.app.domain.Member;
import nextstep.security.userdetails.UserDetails;
import nextstep.security.userdetails.UserDetailsService;
import nextstep.security.exception.BadCredentialsException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class DaoAuthenticationProviderTest {

    UserDetails user1 = new Member("a.a@gmail.com", "valid", "name", "imageUrl");

    @Test
    void authenticateValidUser() {
        // given
        UserDetailsService userDetailsService = Mockito.mock(UserDetailsService.class);
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        Authentication authRequest = new UsernamePasswordAuthenticationToken(
                user1.getUsername(), user1.getPassword());

        // when
        when(userDetailsService.loadUserByUsername(user1.getUsername())).thenReturn(user1);
        Authentication authResult = daoAuthenticationProvider.authenticate(authRequest);

        // then
        assertTrue(authResult.isAuthenticated());
    }

    @Test
    void authenticateInvalidUser() {
        // given
        UserDetailsService userDetailsService = Mockito.mock(UserDetailsService.class);
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        Authentication authRequest = new UsernamePasswordAuthenticationToken(
                user1.getUsername(), "invalid");

        // when
        when(userDetailsService.loadUserByUsername(user1.getUsername()))
                .thenReturn(user1);

        // then
        assertThrows(BadCredentialsException.class,
                () -> daoAuthenticationProvider.authenticate(authRequest));
    }
}
