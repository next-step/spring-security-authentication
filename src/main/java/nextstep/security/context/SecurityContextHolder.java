package nextstep.security.context;

public class SecurityContextHolder {

    private static final ThreadLocal<SecurityContext> contextHolder;

    static {
        System.out.println("SecurityContextHolder.static initializer");
        contextHolder = new ThreadLocal<>();
    }

    public static void clearContext() {
        System.out.println("SecurityContextHolder.clearContext");
        contextHolder.remove();
    }

    public static SecurityContext getContext() {
        System.out.println("SecurityContextHolder.getContext");
        SecurityContext ctx = contextHolder.get();
        if (ctx == null) {
            ctx = createEmptyContext();
            contextHolder.set(ctx);
        }
        return ctx;
    }

    public static void setContext(SecurityContext context) {
        System.out.println("SecurityContextHolder.setContext");
        if (context != null) {
            System.out.println("SecurityContextHolder.setContext - not null");
            contextHolder.set(context);
        }
    }
    public static SecurityContext createEmptyContext() {
        System.out.println("SecurityContextHolder.createEmptyContext");
        return new SecurityContext();
    }
}
