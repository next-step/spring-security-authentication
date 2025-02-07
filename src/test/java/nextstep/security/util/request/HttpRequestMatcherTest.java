package nextstep.security.util.request;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;

class HttpRequestMatcherTest {

    @Test
    @DisplayName("http 요청 메소드와 url 이 일치하는지 확인한다")
    void matchTest() {
        var matcher = new HttpRequestMatcher(HttpMethod.GET , "/login");
        var request = new MockHttpServletRequest(HttpMethod.GET.name() , "/login");
        Assertions.assertThat(matcher.matches(request)).isTrue();
    }

}
