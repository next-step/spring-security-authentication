package nextstep.security.authentication;

public interface Authentication {
    String getPrincipal();

    String getCredentials();

    boolean isAuthenticated();
}
