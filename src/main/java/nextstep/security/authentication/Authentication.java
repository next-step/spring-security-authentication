package nextstep.security.authentication;

public interface Authentication {
    String getPrincipal();

    String getCredentials();

    boolean isAuthenticated();

    void setAuthenticated(boolean b);
}
