package nextstep.security.context;


public class SecurityContextHolder {
    private static final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();

    public static SecurityContext getContext() {
        final SecurityContext context = contextHolder.get();

        if (context == null) {
            return createEmptyContext();
        }

        return context;
    }

    private static SecurityContext createEmptyContext() {
        final SecurityContext emptyContext = new SecurityContext();
        contextHolder.set(emptyContext);

        return emptyContext;
    }

    public static void setContext(SecurityContext context) {
        if (context != null) {
            contextHolder.set(context);
        }
    }

    public static void clearContext() {
        contextHolder.remove();
    }
}
