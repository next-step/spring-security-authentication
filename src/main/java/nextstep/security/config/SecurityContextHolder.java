package nextstep.security.config;

public class SecurityContextHolder {

    private static final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();

    // 현재 스레드의 SecurityContext 반환
    public static SecurityContext getContext() {
        SecurityContext securityContext = contextHolder.get();
        if (securityContext == null) {
            securityContext = new SecurityContextImpl();
            contextHolder.set(securityContext);
        }
        return securityContext;
    }

    // 현재 스레드의 SecurityContext 설정
    public static void setContext(SecurityContext securityContext) {
        contextHolder.set(securityContext);
    }

    //현재 스레드의 SecurityContext 초기화.
    public static void clearContext() {
        contextHolder.remove();
    }

    public static SecurityContext createEmptyContext() {
        return new SecurityContextImpl();
    }

}
