package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.Role;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.core.context.SecurityContextHolder;
import nextstep.security.core.context.SecurityContextRepository;
import nextstep.security.util.Base64Convertor;

import java.io.IOException;

public class BasicAuthFilter extends AbstractAuthProcessingFilter {

    public static final String AUTHORIZATION = "Authorization";

    public BasicAuthFilter(final AuthenticationManager authenticationManager, final SecurityContextRepository securityContextRepository) {
        super(authenticationManager, securityContextRepository);
    }

    @Override
    boolean match(final HttpServletRequest request) {
        return existAuthorizationHeader(request);
    }

    private boolean existAuthorizationHeader(final HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION) != null;
    }

    @Override
    public Authentication makeAuthentication(final HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION);

        String credentials = authorization.split(" ")[1];
        String decodedString = Base64Convertor.decode(credentials);
        String[] usernameAndPassword = decodedString.split(":");
        String username = usernameAndPassword[0];
        String password = usernameAndPassword[1];

        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username,
                password);
        return authRequest;
    }

    @Override
    protected void successAuthentication(final HttpServletRequest request, final HttpServletResponse response,
                                         final FilterChain filterChain) throws ServletException, IOException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.getAuthorities().add(Role.ADMIN);

        filterChain.doFilter(request, response);
    }
}
