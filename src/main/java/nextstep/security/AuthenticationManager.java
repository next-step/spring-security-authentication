package nextstep.security;

public interface AuthenticationManager {
    Authentication authenticate(Authentication authentication);
}