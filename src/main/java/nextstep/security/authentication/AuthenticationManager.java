package nextstep.security.authentication;

public interface AuthenticationManager {
    Authentication authenticate(final Authentication authentication);
}
