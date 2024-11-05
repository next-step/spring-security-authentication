package nextstep.security.context;

public class SecurityContextHolder {
    private static ThreadLocal<SecurityContext> contextHolder;

    static {
        contextHolder = new ThreadLocal<>();
    }

    public static SecurityContext getContext() {
        SecurityContext ctx = contextHolder.get();
        if (ctx == null) {
            ctx = createEmptyContext();
            contextHolder.set(ctx);
        }
        return ctx;
    }
    public static void setContext(SecurityContext context) {
        if (context != null) {
            contextHolder.set(context);
        }
    }
    public static SecurityContext createEmptyContext() {
        return new SecurityContext();
    }

    public static void clearContext() {
        contextHolder.remove();
    }
}
