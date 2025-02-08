package nextstep.security.authentication;

public interface Authentication {
    Object getPrincipal();

    Object getCredentials();

    boolean isAuthenticated();

    void setAuthenticated(boolean b);
}
