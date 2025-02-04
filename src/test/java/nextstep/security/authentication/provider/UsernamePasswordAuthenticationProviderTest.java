package nextstep.security.authentication.provider;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.token.UsernamePasswordAuthenticationToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.security.MockFactory.PASSWORD;
import static nextstep.security.MockFactory.USERNAME;
import static nextstep.security.MockFactory.createUsernamePasswordAuthenticationProvider;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UsernamePasswordAuthenticationProviderTest {
    private final AuthenticationProvider provider = createUsernamePasswordAuthenticationProvider();

    @DisplayName("Provider 를 통해 AuthenticationToken 을 제공할 수 있다.")
    @Test
    void authenticate() {
        final Authentication token = new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD);
        final Authentication authentication = provider.authenticate(token);
        assertAll(
                () -> assertThat(provider.supports(token.getClass()))
                        .isTrue(),
                () -> assertThat(provider.supports(authentication.getClass()))
                        .isTrue(),
                () -> assertThat(authentication.getPrincipal())
                        .isEqualTo(USERNAME),
                () -> assertThat(authentication.getCredentials())
                        .isEqualTo(PASSWORD)
        );
    }

    @DisplayName("Provider 를 통해 제공할 수 없는 AuthenticationToken 이이라면 supports 가 false 이다.")
    @Test
    void notSupported() {
        assertThat(provider.supports(NotSupportedAuthenticationToken.class))
                .isFalse();
    }

    private static final class NotSupportedAuthenticationToken implements Authentication {
        @Override
        public Object getPrincipal() {
            return null;
        }

        @Override
        public Object getCredentials() {
            return null;
        }
    }
}
