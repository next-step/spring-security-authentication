package nextstep.security.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.context.SecurityContextRepository;

public class UsernamePasswordAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final SecurityContextRepository securityContextRepository;

    public UsernamePasswordAuthenticationSuccessHandler(SecurityContextRepository securityContextRepository) {
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
                                        Authentication authentication) {

        SecurityContext ctx = SecurityContextHolder.createEmptyContext();
        ctx.setAuthentication(authentication);

        SecurityContextHolder.setContext(ctx);
        securityContextRepository.saveContext(ctx, httpRequest, httpResponse);
    }
}
