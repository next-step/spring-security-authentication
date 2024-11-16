package nextstep.security.context;

import nextstep.security.domain.UserDetails;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public final class SecurityContextHolder {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    public static UserDetails getUserDetails() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }

        return (UserDetails) attributes.getAttribute(SPRING_SECURITY_CONTEXT_KEY, RequestAttributes.SCOPE_SESSION);
    }

    public static void setUserDetails(UserDetails userDetails) {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }

        attributes.setAttribute(SPRING_SECURITY_CONTEXT_KEY, userDetails, RequestAttributes.SCOPE_SESSION);
    }
}
