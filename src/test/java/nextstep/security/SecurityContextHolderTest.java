package nextstep.security;

import nextstep.security.fixture.TestAuthentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityContextHolderTest {

    private final Authentication authentication = new TestAuthentication("admin","name", true);

    @Test
    @DisplayName("설정하지않는 getContext 를 호출하면 null을 반환하지 않는다")
    void isNotNull() {
        SecurityContext context = SecurityContextHolder.getContext();

        assertThat(context).isNotNull();
    }

    @Test
    @DisplayName("SecurityContextHolder 에 null 을 설정해도 null이 설정되지 않는다")
    void isNotNull2() {
        SecurityContextHolder.setContext(null);

        assertThat(SecurityContextHolder.getContext()).isNotNull();
    }

    @Test
    @DisplayName("clearContext 를 호출하면 인증정보가 비어진다")
    void clearContext()  {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);

        SecurityContextHolder.clearContext();

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    @DisplayName("SecurityContextHolder가 관리하지 않은 별도의 SecurityContext를 생성한다")
    void createEmptyContext() {
        SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
        emptyContext.setAuthentication(authentication);


        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

}
