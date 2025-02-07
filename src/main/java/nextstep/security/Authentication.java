package nextstep.security;

public interface Authentication {

    String getPrincipal();

    String getCredentials();

    boolean isAuthenticated();
}
