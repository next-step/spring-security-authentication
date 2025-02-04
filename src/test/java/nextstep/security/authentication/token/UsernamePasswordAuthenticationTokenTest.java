package nextstep.security.authentication.token;

import nextstep.security.authentication.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.security.Fixture.PASSWORD;
import static nextstep.security.Fixture.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;

class UsernamePasswordAuthenticationTokenTest {
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        authentication = new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD);
    }

    @DisplayName("유저 식별에 사용되는 username 은 principal 이다.")
    @Test
    void getPrincipal() {
        assertThat(
                authentication.getPrincipal()
        ).isEqualTo(USERNAME);
    }

    @DisplayName("식별된 유저의 검증에 사용되는 password 은 credential 이다.")
    @Test
    void getCredentials() {
        assertThat(
                authentication.getPrincipal()
        ).isEqualTo(USERNAME);
    }
}
