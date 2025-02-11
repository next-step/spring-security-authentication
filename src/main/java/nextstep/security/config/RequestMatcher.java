package nextstep.security.config;


import jakarta.servlet.http.HttpServletRequest;

public interface RequestMatcher {

    boolean matches(HttpServletRequest request);


}
