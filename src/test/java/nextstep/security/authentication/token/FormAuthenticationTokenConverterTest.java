package nextstep.security.authentication.token;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static nextstep.security.Fixture.PASSWORD;
import static nextstep.security.Fixture.USERNAME;
import static nextstep.security.Fixture.createLoginRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class FormAuthenticationTokenConverterTest {
    private final AuthenticationTokenConverter converter = FormAuthenticationTokenConverter.getInstance();

    @DisplayName("form 형태를 authentication token 으로 만들 수 있다.")
    @Test
    void of() {
        final HttpServletRequest request = createLoginRequest();
        final Authentication authentication = converter.convert(request);
        assertAll(
                () -> assertThat(converter.supports(request))
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
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/login");
        assertThat(converter.supports(request))
                .isFalse();
    }
}
