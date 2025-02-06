package nextstep.security.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.context.SecurityContextHolderStrategy;
import nextstep.security.context.SecurityContextRepository;

public class BasicAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final SecurityContextHolderStrategy securityContextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();
    private final SecurityContextRepository securityContextRepository;

    public BasicAuthenticationSuccessHandler(SecurityContextRepository securityContextRepository) {
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
                                        Authentication authentication) {

        SecurityContext ctx = this.securityContextHolderStrategy.createEmptyContext();
        ctx.setAuthentication(authentication);

        this.securityContextHolderStrategy.setContext(ctx);
        this.securityContextRepository.saveContext(ctx, httpRequest, httpResponse);
    }
}
