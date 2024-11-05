package nextstep.security.service.context;

import java.util.Objects;

import nextstep.security.model.context.SecurityContext;

public class SecurityContextHolder {

    private static final ThreadLocal<SecurityContext> contextHolder;

    static {
        contextHolder = new ThreadLocal<>();
    }

    public static void clearContext() {
        contextHolder.remove();
    }

    public static void setContext(SecurityContext context) {
        if (Objects.nonNull(context)) {
            contextHolder.set(context);
        }
    }

    public static SecurityContext getContext() {
        if (Objects.nonNull(contextHolder.get())) {
            return contextHolder.get();
        } else {
            SecurityContext emptyContext = createEmptyContext();
            contextHolder.set(emptyContext);
            return emptyContext;
        }
    }

    public static SecurityContext createEmptyContext() {
        return new SecurityContext();
    }

}
