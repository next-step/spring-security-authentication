package nextstep.security.authentication;

public class SecurityContextHolder {
    private static final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();

    public static SecurityContext getContext() {
        if (contextHolder.get() == null) {
            setContext(new SecurityContextImpl());
        }
        return contextHolder.get();
    }

    public static void setContext(SecurityContext context) {
        contextHolder.set(context);
    }

    public static void clearContext() {
        contextHolder.remove();
    }
}
