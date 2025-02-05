package nextstep.security.authentication;

import nextstep.app.domain.Member;
import nextstep.security.userdetails.UserDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

class ProviderManagerTest {

    UserDetails user1 = new Member("a.a@gmail.com", "valid", "name", "imageUrl");

    @Test
    void delegateToMatchedProvider() {

        // given
        AuthenticationProvider provider1 = mock(AuthenticationProvider.class);
        AuthenticationProvider provider2 = mock(AuthenticationProvider.class);
        ProviderManager providerManager = new ProviderManager(List.of(provider1, provider2));

        // when
        when(provider1.supports(any())).thenReturn(false);
        when(provider2.supports(any())).thenReturn(true);
        when(provider2.authenticate(any())).thenReturn(
                UsernamePasswordAuthenticationToken.authenticated(user1));

        Authentication authentication = mock(Authentication.class);
        Authentication result = providerManager.authenticate(authentication);

        // then
        Assertions.assertNotNull(result);
        verify(provider1, never()).authenticate(any());
        verify(provider2, times(1)).authenticate(any());
    }
}
