package nextstep.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * SecurityContextHolderFilter는 요청 시작 시 SecurityContextRepository로부터 인증 정보를 SecurityContextHolder로 이동시킴
 * 요청 종료 후 SecurityContextHolder를 정리.
 * 요청 시작 시:
 * SecurityContextRepository.loadContext() 호출.
 * 로드한 인증 정보를 SecurityContextHolder에 설정.
 * 요청 종료 시:
 * SecurityContextHolder.getContext()를 가져와 SecurityContextRepository.saveContext() 호출.
 * SecurityContextHolder.clearContext()로 정리.
 */
public class SecurityContextHolderFilter extends GenericFilterBean {

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        boolean isHttpServlet = servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse;

        if (isHttpServlet) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            SecurityContext securityContext = securityContextRepository.loadContext(request);
            try {
                SecurityContextHolder.setContext(securityContext);
                chain.doFilter(servletRequest, servletResponse);
            } finally {
                SecurityContextHolder.clearContext();
            }
            return;
        }
        throw new ServletException("SecurityContextHolderFilter only supports HTTP requests");

    }

}
