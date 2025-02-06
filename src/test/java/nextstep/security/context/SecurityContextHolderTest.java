package nextstep.security.context;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecurityContextHolderTest {

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getContext_ShouldReturnNewContext_WhenNoContextIsSet() {
        SecurityContext context = SecurityContextHolder.getContext();

        assertNotNull(context);
        assertInstanceOf(SecurityContextImpl.class, context);
    }

    @Test
    void getContext_ShouldReturnSameContext_WhenCalledMultipleTimes() {
        SecurityContext firstCall = SecurityContextHolder.getContext();
        SecurityContext secondCall = SecurityContextHolder.getContext();

        assertSame(firstCall, secondCall);
    }

    @Test
    void setContext_ShouldOverrideExistingContext() {
        SecurityContext securityContext = new SecurityContextImpl();
        SecurityContextHolder.setContext(securityContext);

        assertSame(securityContext, SecurityContextHolder.getContext());
    }

    @Test
    void clearContext_ShouldRemoveContext() {
        SecurityContext securityContext = new SecurityContextImpl();
        SecurityContextHolder.setContext(securityContext);
        SecurityContextHolder.clearContext();

        assertNotSame(SecurityContextHolder.getContext(), SecurityContextHolder.createEmptyContext());
        assertInstanceOf(SecurityContextImpl.class, SecurityContextHolder.getContext());
    }
}
