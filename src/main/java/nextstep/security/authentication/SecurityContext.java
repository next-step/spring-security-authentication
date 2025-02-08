package nextstep.security.authentication;

public interface SecurityContext {
    Authentication getAuthentication();

    void setAuthentication(Authentication authentication);
}
