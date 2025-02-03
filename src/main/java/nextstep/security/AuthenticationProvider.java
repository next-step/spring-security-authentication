package nextstep.security;

public interface AuthenticationProvider {

    Authentication authenticate(Authentication authentication);

    boolean supports(Class<?> authentication);
}
