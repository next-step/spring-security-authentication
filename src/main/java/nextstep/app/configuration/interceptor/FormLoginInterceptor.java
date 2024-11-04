package nextstep.app.configuration.interceptor;

import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static nextstep.app.utils.Constants.PASSWORD_ATTRIBUTE_NAME;
import static nextstep.app.utils.Constants.SPRING_SECURITY_CONTEXT_KEY;
import static nextstep.app.utils.Constants.USERNAME_ATTRIBUTE_NAME;

@Component
@RequiredArgsConstructor
public class FormLoginInterceptor implements HandlerInterceptor {

    private final MemberRepository memberRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();

        if (Objects.nonNull(session.getAttribute(SPRING_SECURITY_CONTEXT_KEY))) {
            Member member = (Member) session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
            return memberRepository.findByEmailAndPassword(member.getEmail(), member.getPassword()).isPresent();
        }

        Optional<Member> member = memberRepository.findByEmailAndPassword(
                request.getParameter(USERNAME_ATTRIBUTE_NAME),
                request.getParameter(PASSWORD_ATTRIBUTE_NAME));

        if (member.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, member.get());
        return true;
    }

}
