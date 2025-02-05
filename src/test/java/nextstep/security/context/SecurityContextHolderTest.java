package nextstep.security.context;

import nextstep.security.SecurityContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

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
        SecurityContext mockContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(mockContext);

        assertSame(mockContext, SecurityContextHolder.getContext());
    }

    @Test
    void clearContext_ShouldRemoveContext() {
        SecurityContextHolder.setContext(mock(SecurityContext.class));
        SecurityContextHolder.clearContext();

        assertNotSame(SecurityContextHolder.getContext(), SecurityContextHolder.createEmptyContext());
        assertInstanceOf(SecurityContextImpl.class, SecurityContextHolder.getContext());
    }
}
