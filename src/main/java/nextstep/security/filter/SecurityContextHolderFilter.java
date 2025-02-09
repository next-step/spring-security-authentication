package nextstep.security.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.context.SecurityContext;
import nextstep.security.authentication.context.SecurityContextHolder;
import nextstep.security.authentication.provider.AuthenticationManager;

public class SecurityContextHolderFilter extends AbstractAuthenticationFilter{

    public SecurityContextHolderFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        SecurityContext securityContext = securityContextRepository.loadContext(request);
        if(securityContext == null) {
            return null;
        }
        SecurityContextHolder.setContext(securityContext);
        return securityContext.getAuthentication();
    }

    @Override
    public boolean support(HttpServletRequest request) {
        return true;
    }
}
