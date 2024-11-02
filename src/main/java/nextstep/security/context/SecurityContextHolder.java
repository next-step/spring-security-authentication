package nextstep.security.context;

import java.util.function.Supplier;

public class SecurityContextHolder {
    private static final ThreadLocal<Supplier<SecurityContext>> contextHolder = new ThreadLocal<>();

    public SecurityContext getContext() {
        return contextHolder.get().get();
    }

    public void setContext(SecurityContext context) {
        contextHolder.set(() -> context);
    }

    public void clearContext() {
        contextHolder.remove();
    }
}

