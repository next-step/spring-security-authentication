package nextstep.security;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.context.HttpSessionSecurityContextRepository;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;

import static nextstep.security.Fixture.PASSWORD;
import static nextstep.security.Fixture.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public final class Steps {
    private Steps() {}

    public static void testAuthentication(HttpServletRequest request) {
        final SecurityContext context = SecurityContextHolder.getContext();
        final Authentication authentication = context.getAuthentication();
        assertAll(
                () -> assertThat(context)
                        .isNotEqualTo(SecurityContext.empty()),
                () -> assertThat(context)
                        .isEqualTo(HttpSessionSecurityContextRepository.getInstance().loadContext(request)),
                () -> assertThat(authentication.getPrincipal())
                        .isEqualTo(USERNAME),
                () -> assertThat(authentication.getCredentials())
                        .isEqualTo(PASSWORD)
        );
    }
}
