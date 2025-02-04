package nextstep.security.authentication.token.basic;

import nextstep.security.authentication.Authentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static nextstep.security.authentication.MockFactory.PASSWORD;
import static nextstep.security.authentication.MockFactory.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class BasicAuthenticationTokenTest {

    @DisplayName("Basic token 을 디코딩할 수 있다.")
    @Test
    void of() {
        final String basicToken = encode(USERNAME, PASSWORD);
        final Authentication authentication = BasicAuthenticationToken.of(
                encode(USERNAME, PASSWORD)
        );
        assertAll(
                () -> assertThat(BasicAuthenticationToken.supports(basicToken))
                        .isTrue(),
                () -> assertThat(authentication.getPrincipal())
                        .isEqualTo(USERNAME),
                () -> assertThat(authentication.getCredentials())
                        .isEqualTo(PASSWORD)
        );
    }

    @DisplayName("잘못된 Basic token 은 지원하지 않는다.")
    @Test
    void notSupported() {
        assertThat(BasicAuthenticationToken.supports("invalid"))
                .isFalse();
    }

    private String encode(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString(
                (username + ":" + password).getBytes()
        );
    }
}
