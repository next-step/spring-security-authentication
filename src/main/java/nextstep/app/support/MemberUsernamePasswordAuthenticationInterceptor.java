package nextstep.app.support;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.ui.AuthenticationException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class MemberUsernamePasswordAuthenticationInterceptor implements HandlerInterceptor {
    private final MemberRepository memberRepository;

    public MemberUsernamePasswordAuthenticationInterceptor(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) throws Exception {
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            throw new AuthenticationException();
        }

        final String usernameAndPassword = new String(
            Base64.getDecoder().decode(header.split(" ")[1]),
            StandardCharsets.UTF_8
        );
        final String username = usernameAndPassword.split(":")[0];
        final String password = usernameAndPassword.split(":")[1];

        final Member member = memberRepository.findByEmail(username)
            .orElseThrow(AuthenticationException::new);

        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        final Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
