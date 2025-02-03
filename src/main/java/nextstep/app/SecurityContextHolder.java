package nextstep.app;

import nextstep.security.SecurityContext;

public class SecurityContextHolder {

    protected SecurityContextHolder() {
        throw new UnsupportedOperationException();
    }

    private static final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();

    public static SecurityContext getContext() {
        SecurityContext context = contextHolder.get();
        if (context == null) {
            context = SecurityContextImpl.empty();
            contextHolder.set(context);
        }
        return context;
    }

    public static void setContext(SecurityContext context) {
        contextHolder.set(context);
    }

    public static void clearContext() {
        contextHolder.remove();
    }
}