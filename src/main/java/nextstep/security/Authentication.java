package nextstep.security;

public interface Authentication {
    Object getPrincipal();
    Object getCredentials();
    boolean isAuthenticated();
}
