package nextstep.security.authentication;

import nextstep.app.domain.Member;
import nextstep.security.userdetails.UserDetails;
import nextstep.security.userdetails.UserDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class ProviderManagerTest {

    UserDetails user1 = new Member("a.a@gmail.com", "valid", "name", "imageUrl");
    UserDetailsService userDetailsService = username -> {
        if (user1.getUsername().equals(username)) {
            return user1;
        }

        throw new IllegalStateException("User not found");
    };

    @Test
    void delegateToMatchedProvider() {

        ProviderManager providerManager = new ProviderManager(
                List.of(
                        new DaoAuthenticationProvider(userDetailsService),
                        new TestAuthenticationProvider()
                )
        );
        Authentication result = providerManager.authenticate(
                new UsernamePasswordAuthenticationToken(user1.getUsername(), user1.getPassword()));

        Assertions.assertTrue(result.isAuthenticated());
    }

    static class TestAuthenticationProvider implements AuthenticationProvider {

        @Override
        public Authentication authenticate(Authentication authentication) {
            return authentication;
        }

        @Override
        public boolean supports(Class<?> authentication) {
            return false;
        }
    }
}
