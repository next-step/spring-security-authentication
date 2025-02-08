package nextstep.security.authentication;


import nextstep.security.fixture.TestUserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class DaoAuthenticationProviderTest {
    @Test
    @DisplayName("daoAuthenticationProvider 는 UsernamePasswordAuthenticationToken 을 지원한다")
    void support() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider((it) -> null);

        assertSoftly(it -> {
            it.assertThat(daoAuthenticationProvider.supports(UsernamePasswordAuthenticationToken.class)).isTrue();
            it.assertThat(daoAuthenticationProvider.supports(Authentication.class)).isFalse();
        });
    }

    @Test
    @DisplayName("daoAuthenticationProvider 는 UsernamePasswordAuthenticationToken 하위 타입을 지원한다")
    void support_extends() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider((it) -> null);

        assertSoftly(it -> {
            it.assertThat(daoAuthenticationProvider.supports(ChildUsernamePasswordAuthenticationToken.class)).isTrue();
            it.assertThat(daoAuthenticationProvider.supports(UsernamePasswordAuthenticationToken.class)).isTrue();
        });
    }

    @Test
    @DisplayName("authentication 가 비어있으면 예외를 발생한다")
    void noAuthentication() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider((it) -> null);

        assertThatExceptionOfType(AuthenticationException.class).isThrownBy(
                () -> daoAuthenticationProvider.authenticate(null)
        );
    }

    @Test
    @DisplayName("principal 이 비어있으면 예외가 발생한다")
    void noPrincipal() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider((it) -> null);

        assertThatExceptionOfType(AuthenticationException.class).isThrownBy(
                () -> daoAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(null, ""))
        );
    }

    @Test
    @DisplayName("credentials이 비어 있으면 예외가 발생한다")
    void noCredentials() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider((it) -> null);

        assertThatExceptionOfType(AuthenticationException.class).isThrownBy(
                () -> daoAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken("", null))
        );
    }

    @Test
    @DisplayName("자격증명이 다른경우 예외를 발생한다")
    void notValidCredentials() {
        String password = "password";
        TestUserDetails userDetail = new TestUserDetails("id", password);

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider((it) -> userDetail);

        assertThatExceptionOfType(AuthenticationException.class).isThrownBy(
                () -> daoAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken("id", "noPassword"))
        );
    }

    @Test
    @DisplayName("자격증명이 올바른경우 인증된 authentication 가 반환된다")
    void authenticate() {
        String password = "password";
        TestUserDetails userDetail = new TestUserDetails("id", password);
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider((it) -> userDetail);

        Authentication authenticate = daoAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken("", password));

        assertThat(authenticate.isAuthenticated()).isTrue();
    }

    private static class ChildUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {
        public ChildUsernamePasswordAuthenticationToken(String principal, String credentials) {
            super(principal, credentials);
        }
    }

}
