package nextstep.security.configuration.authentication;

public interface Authentication {

    Object getCredentials();

    Object getPrincipal();

    boolean isAuthenticated();

}
