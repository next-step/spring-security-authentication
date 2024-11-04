package nextstep.security.configuration.interceptor;

import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import nextstep.security.model.UserDetails;
import nextstep.security.service.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static nextstep.security.utils.Constants.PASSWORD_ATTRIBUTE_NAME;
import static nextstep.security.utils.Constants.SPRING_SECURITY_CONTEXT_KEY;
import static nextstep.security.utils.Constants.USERNAME_ATTRIBUTE_NAME;

@Component
@RequiredArgsConstructor
public class FormLoginInterceptor implements HandlerInterceptor {

    private final UserDetailsService userDetailsService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();

        if (Objects.nonNull(session.getAttribute(SPRING_SECURITY_CONTEXT_KEY))) {
            UserDetails userDetails = (UserDetails) session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
            return userDetailsService.loadUserByUsernameAndEmail(userDetails.getUserName(), userDetails.getPassword())
                    .isPresent();
        }

        Optional<UserDetails> userDetails = userDetailsService.loadUserByUsernameAndEmail(
                request.getParameter(USERNAME_ATTRIBUTE_NAME),
                request.getParameter(PASSWORD_ATTRIBUTE_NAME));

        if (userDetails.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, userDetails.get());
        return true;
    }

}
