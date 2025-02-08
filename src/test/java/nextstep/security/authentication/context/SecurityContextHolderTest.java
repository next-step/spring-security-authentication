package nextstep.security.authentication.context;

import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityContextHolderTest {

    private SecurityContext testContext = SecurityContextImpl.from(
            new UsernamePasswordAuthenticationToken("username", "password")
    );

    @DisplayName("SecurityContext가 저장된 값을 반환한다.")
    @Test
    void getContext() {
        SecurityContextHolder.setContext(testContext);
        SecurityContext context = SecurityContextHolder.getContext();

        assertThat(context).isEqualTo(testContext);
    }

    @DisplayName("SecurityContext가 없으면 새로운 SecurityContext를 생성하여 반환한다.")
    @Test
    void getContextWhenContextIsNull() {
        SecurityContext context = SecurityContextHolder.getContext();

        assertThat(context).isNotNull();
    }

    @DisplayName("SecurityContext 저장후 제거 후에는 새로운 SecurityContext을 반환한다.")
    @Test
    void getContextAfterClearContext() {
        SecurityContextHolder.setContext(testContext);
        SecurityContextHolder.clearContext();
        SecurityContext context = SecurityContextHolder.getContext();

        assertThat(context).isNotEqualTo(testContext);
    }

}
