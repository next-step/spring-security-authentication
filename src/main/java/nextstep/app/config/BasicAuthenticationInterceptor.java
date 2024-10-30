package nextstep.app.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import nextstep.app.service.BasicAuthenticationService;
import nextstep.domain.Member;
import nextstep.domain.MemberRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class BasicAuthenticationInterceptor implements HandlerInterceptor {

    private final MemberRepository memberRepository;
    private final BasicAuthenticationService basicAuthenticationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isNotEmpty(authorizationHeader) && authorizationHeader.startsWith("Basic ")) {
            Member decodedMember = basicAuthenticationService.decodeAuthorization(authorizationHeader);
            return memberRepository.findByEmail(decodedMember.getEmail()).isEmpty();
        }
        return false;
    }

}
