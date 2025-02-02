package nextstep.security.context;

public class SecurityContextHolder {
    private static final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();

    public static void clearContext() {
        contextHolder.remove();
    }

    public static SecurityContext getContext() {
        final SecurityContext context = contextHolder.get();
        return context != null
                ? context
                : SecurityContext.empty();
    }

    public static void setContext(SecurityContext context) {
        contextHolder.set(context);
    }
}
