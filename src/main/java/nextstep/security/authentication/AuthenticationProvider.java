package nextstep.security.authentication;

public interface AuthenticationProvider {

    Authentication authenticate(Authentication Authentication);

    boolean supports(Authentication authentication);
}
