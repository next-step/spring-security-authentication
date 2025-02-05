package nextstep.security;

public interface SecurityContext {
    Authentication getAuthentication();
    void setAuthentication(Authentication authentication);
}
