package nextstep.security.context;

import nextstep.security.authentication.Authentication;

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

    public static SecurityContext createContextBy(Authentication authentication) {
        SecurityContext context = getContext();
        context.setAuthentication(authentication);
        return context;
    }

    public static void setContext(SecurityContext context) {
        contextHolder.set(context);
    }

    public static void clearContext() {
        contextHolder.remove();
    }
}
