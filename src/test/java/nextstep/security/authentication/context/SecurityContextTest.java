package nextstep.security.authentication.context;

import nextstep.util.AuthenticationFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityContextTest {

    @DisplayName("SecurityContextImpl 객체를 생성한다.")
    @Test
    void createSecurityContext() {
        SecurityContextImpl securityContext = SecurityContextImpl.from(AuthenticationFixture.createAuthentication());

        assertThat(securityContext).isNotNull();
    }


}
