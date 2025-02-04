package nextstep.security.context;

import nextstep.security.authentication.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.security.authentication.MockFactory.PASSWORD;
import static nextstep.security.authentication.MockFactory.USERNAME;
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
        SecurityContextHolder.setContext(new SecurityContext(
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
        ));
        final Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        assertAll(
                () -> assertThat(authentication.getPrincipal())
                        .isEqualTo(USERNAME),
                () -> assertThat(authentication.getCredentials())
                        .isEqualTo(PASSWORD)
        );
    }
}
