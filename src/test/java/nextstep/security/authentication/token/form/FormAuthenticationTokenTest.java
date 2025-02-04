package nextstep.security.authentication.token.form;

import nextstep.security.authentication.Authentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static nextstep.security.MockFactory.PASSWORD;
import static nextstep.security.MockFactory.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class FormAuthenticationTokenTest {

    @DisplayName("form 형태를 authentication token 으로 만들 수 있다.")
    @Test
    void of() {
        final Map<String, String[]> parameterMap = Map.of(
                "username", new String[]{USERNAME},
                "password", new String[]{PASSWORD}
        );
        final Authentication authentication = FormAuthenticationToken.of(parameterMap);
        assertAll(
                () -> assertThat(FormAuthenticationToken.supports(parameterMap))
                        .isTrue(),
                () -> assertThat(authentication.getPrincipal())
                        .isEqualTo(USERNAME),
                () -> assertThat(authentication.getCredentials())
                        .isEqualTo(PASSWORD)
        );
    }

    @DisplayName("username 과 password 가 없는 form 은 지원하지 않는다.")
    @Test
    void notSupported() {
        assertThat(FormAuthenticationToken.supports(Map.of()))
                .isFalse();
    }
}
