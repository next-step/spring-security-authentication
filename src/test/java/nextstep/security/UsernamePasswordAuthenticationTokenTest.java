package nextstep.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class UsernamePasswordAuthenticationTokenTest {
    @Test
    @DisplayName("UsernamePasswordAuthenticationToken 은 정상적으로 생성된다")
    void create() {
        String username = "username";
        String password = "password";

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        assertSoftly(it -> {
            it.assertThat(usernamePasswordAuthenticationToken.getPrincipal()).isEqualTo(username);
            it.assertThat(usernamePasswordAuthenticationToken.getCredentials()).isEqualTo(password);
            it.assertThat(usernamePasswordAuthenticationToken.isAuthenticated()).isFalse();
        });
    }

    @Test
    @DisplayName("ofAuthenticated 메서드는 인증된 Authentication 을 반환한다.")
    void ofAuthenticated() {
        String username = "username";
        String password = "password";

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken.ofAuthenticated(username, password);

        assertSoftly(it -> {
            it.assertThat(usernamePasswordAuthenticationToken.getPrincipal()).isEqualTo(username);
            it.assertThat(usernamePasswordAuthenticationToken.getCredentials()).isEqualTo(password);
            it.assertThat(usernamePasswordAuthenticationToken.isAuthenticated()).isTrue();
        });
    }

}
