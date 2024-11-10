package nextstep.security.authentication;

import nextstep.security.userdetails.UserDetailsService;
import nextstep.security.context.HttpSessionSecurityContextRepository;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UsernamePasswordAuthenticationFilter extends GenericFilterBean {

    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
    private static final String DEFAULT_REQUEST_URI = "/login";

    private final AuthenticationManager authenticationManager;
    private final HttpSessionSecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    public UsernamePasswordAuthenticationFilter(UserDetailsService userDetailsService) {
        System.out.println("UsernamePasswordAuthenticationFilter.UsernamePasswordAuthenticationFilter");
        this.authenticationManager = new ProviderManager(List.of(new DaoAuthenticationProvider(userDetailsService)));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("UsernamePasswordAuthenticationFilter.doFilter");
        if (!DEFAULT_REQUEST_URI.equals(((HttpServletRequest) request).getRequestURI())) {
            System.out.println("DEFAULT_REQUEST_URI = " + DEFAULT_REQUEST_URI);
            chain.doFilter(request, response);
            return;
        }
        try {
            Authentication authentication = convert(request);
            if(authentication == null) {
                chain.doFilter(request, response);
                return;
            }

            Authentication authenticate = authenticationManager.authenticate(authentication);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authenticate);
            SecurityContextHolder.setContext(context);

//            HttpSession session = ((HttpServletRequest) request).getSession();
//            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, authenticate);
            securityContextRepository.saveContext(context, (HttpServletRequest) request, (HttpServletResponse) response);

        } catch (AuthenticationException e) {
            System.out.println("UsernamePasswordAuthenticationFilter-Exception");
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private Authentication convert(ServletRequest request) {
        System.out.println("UsernamePasswordAuthenticationFilter.convert");
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            String username = parameterMap.get("username")[0];
            String password = parameterMap.get("password")[0];
            return UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        } catch (Exception e) {
            return null;
        }
    }
}
