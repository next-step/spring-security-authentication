package nextstep.security.authentication.convertor;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;

public interface AuthenticationConvertor {
    Authentication convert(HttpServletRequest request);
}
