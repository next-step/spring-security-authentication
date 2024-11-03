package nextstep.app.configuration.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.service.auth.BasicAuthenticationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static nextstep.app.utils.Constants.BASIC_TOKEN_PREFIX;
import static nextstep.app.utils.Constants.SPRING_SECURITY_CONTEXT_KEY;

@Component
@RequiredArgsConstructor
public class BasicAuthenticationInterceptor implements HandlerInterceptor {

    private final MemberRepository memberRepository;
    private final BasicAuthenticationService basicAuthenticationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isEmpty(authorizationHeader) || !authorizationHeader.startsWith(BASIC_TOKEN_PREFIX)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        Member decodedMember = basicAuthenticationService.decodeToken(authorizationHeader);

        if (decodedMember == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, decodedMember);
        return memberRepository.findByEmail(decodedMember.getEmail()).isPresent();
    }

}
