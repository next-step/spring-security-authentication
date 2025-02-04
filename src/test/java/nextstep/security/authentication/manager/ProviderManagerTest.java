package nextstep.security.authentication.manager;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.token.UsernamePasswordAuthenticationToken;
import nextstep.security.exception.AuthenticationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.security.authentication.MockFactory.PASSWORD;
import static nextstep.security.authentication.MockFactory.USERNAME;
import static nextstep.security.authentication.MockFactory.createProviderManager;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

class ProviderManagerTest {
    private final AuthenticationManager manager = createProviderManager();

    @DisplayName("Manager 를 통해 AuthenticationToken 을 제공할 수 있다.")
    @Test
    void authenticate() {
        final Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD)
        );
        assertAll(
                () -> assertThat(authentication.getPrincipal())
                        .isEqualTo(USERNAME),
                () -> assertThat(authentication.getCredentials())
                        .isEqualTo(PASSWORD)
        );
    }

    @DisplayName("AuthenticationToken 제공에 실패하면 AuthenticationException 이 발생한다.")
    @Test
    void failAuthentication() {
        final Authentication invalidToken = new Authentication() {
            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }
        };
        assertThatExceptionOfType(AuthenticationException.class)
                .isThrownBy(() -> manager.authenticate(invalidToken));
    }
}
