package nextstep.security.authentication;

public interface Authentication {

    Object getCredentials();

    Object getPrincipal();

    Object isAuthenticated();

}
