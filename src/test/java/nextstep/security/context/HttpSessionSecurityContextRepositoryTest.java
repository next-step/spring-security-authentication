package nextstep.security.context;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static nextstep.security.Fixture.PASSWORD;
import static nextstep.security.Fixture.USERNAME;
import static nextstep.security.Fixture.createSecurityContext;
import static org.assertj.core.api.Assertions.assertThat;

class HttpSessionSecurityContextRepositoryTest {
    private final SecurityContextRepository repository = HttpSessionSecurityContextRepository.getInstance();

    @DisplayName("최초에는 SecurityContextRepository 에 empty SecurityContext 가 있다.")
    @Test
    void emptyContext() {
        assertThat(repository.loadContext(new MockHttpServletRequest()))
                .isEqualTo(SecurityContext.empty());
    }

    @DisplayName("ServletRequest 가 같으면, SecurityContext 도 같다.")
    @Test
    void sameContext() {
        final HttpServletRequest request = new MockHttpServletRequest();
        final SecurityContext context = createSecurityContext(USERNAME, PASSWORD);
        repository.saveContext(context, request, new MockHttpServletResponse());
        assertThat(repository.loadContext(request))
                .isEqualTo(context);
    }

    @DisplayName("ServletRequest 가 다르면, SecurityContext 도 다르다.")
    @Test
    void differentContext() {
        final SecurityContext context = createSecurityContext(USERNAME, PASSWORD);
        repository.saveContext(context, new MockHttpServletRequest(), new MockHttpServletResponse());
        assertThat(repository.loadContext(new MockHttpServletRequest()))
                .isNotEqualTo(context);
    }

}
