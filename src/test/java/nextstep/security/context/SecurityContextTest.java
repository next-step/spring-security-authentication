package nextstep.security.context;

import nextstep.security.authentication.Authentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.security.authentication.MockFactory.PASSWORD;
import static nextstep.security.authentication.MockFactory.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SecurityContextTest {
    @DisplayName("Empty Security Context 는 빈 principal 과 credential 을 가진다.")
    @Test
    void empty() {
        final Authentication authentication = SecurityContext.empty()
                .getAuthentication();
        assertAll(
                () -> assertThat(authentication.getPrincipal())
                        .isEqualTo(""),
                () -> assertThat(authentication.getCredentials())
                        .isEqualTo("")
        );
    }

    @DisplayName("SecurityContext 는 인증정보(Authentication)을 가진다.")
    @Test
    void getAuthentication() {
        final Authentication authentication = new SecurityContext(
                new Authentication() {
                    @Override
                    public Object getPrincipal() {
                        return USERNAME;
                    }

                    @Override
                    public Object getCredentials() {
                        return PASSWORD;
                    }
                }
        ).getAuthentication();
        assertAll(
                () -> assertThat(authentication.getPrincipal())
                        .isEqualTo(USERNAME),
                () -> assertThat(authentication.getCredentials())
                        .isEqualTo(PASSWORD)
        );
    }
}
