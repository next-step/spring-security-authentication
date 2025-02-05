package nextstep.security.authentication.token;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static nextstep.security.Fixture.PASSWORD;
import static nextstep.security.Fixture.USERNAME;
import static nextstep.security.Fixture.createBasicRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class BasicAuthenticationTokenConverterTest {

    private final AuthenticationTokenConverter converter = BasicAuthenticationTokenConverter.getInstance();

    @DisplayName("Basic token 을 디코딩할 수 있다.")
    @Test
    void of() {
        final HttpServletRequest request = createBasicRequest();
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

    @DisplayName("잘못된 Basic token 은 지원하지 않는다.")
    @Test
    void notSupported() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "invalid token");
        request.setRequestURI("/members");
        assertThat(converter.supports(request))
                .isFalse();
    }
}
