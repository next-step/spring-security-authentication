package nextstep.security.support;

public interface Authentication {
    Object getPrincipal();

    Object getCredentials();

    boolean isAuthenticated();
}
