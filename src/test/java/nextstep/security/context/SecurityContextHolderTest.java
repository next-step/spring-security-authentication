package nextstep.security.context;

import nextstep.security.authentication.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.security.Fixture.PASSWORD;
import static nextstep.security.Fixture.USERNAME;
import static nextstep.security.Fixture.createAuthentication;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SecurityContextHolderTest {

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @DisplayName("SecurityContextHolder 가 clear 된 이후에는 empty SecurityContext 를 가지게 된다.")
    @Test
    void emptyContext() {
        assertThat(SecurityContextHolder.getContext())
                .isEqualTo(SecurityContext.empty());
    }

    @DisplayName("SecurityContextHolder 의 SecurityContext 를 수정할 수 있다.")
    @Test
    void setContext() {
        final Authentication authentication = createAuthentication(USERNAME, PASSWORD);
        final SecurityContext context = new SecurityContext(authentication);
        SecurityContextHolder.setContext(context);
        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isEqualTo(authentication);
        assertAll(
                () -> assertThat(SecurityContextHolder.getContext())
                        .isNotEqualTo(SecurityContext.empty()),
                () -> assertThat(SecurityContextHolder.getContext())
                        .isEqualTo(context),
                () -> assertThat(SecurityContextHolder.getContext().getAuthentication())
                        .isEqualTo(authentication)
        );
    }
}
