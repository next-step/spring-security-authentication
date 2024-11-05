package nextstep.security.context;

import nextstep.security.userdetails.UserDetails;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import static nextstep.security.interceptor.BasicAuthenticationInterceptor.SPRING_SECURITY_CONTEXT;

public final class UserContextHolder {

    private UserContextHolder() {}

    public static UserDetails getUser() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        return (UserDetails) attributes.getAttribute(SPRING_SECURITY_CONTEXT, RequestAttributes.SCOPE_SESSION);
    }

    public static void setUser(UserDetails userDetails) {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        attributes.setAttribute(SPRING_SECURITY_CONTEXT, userDetails, RequestAttributes.SCOPE_SESSION);
    }
}
