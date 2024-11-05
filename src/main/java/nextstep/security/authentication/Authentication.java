package nextstep.security.authentication;

public interface Authentication {
    Object getCredentials(); // token password
    Object getPrincipal(); /// token userName
    boolean isAuthenticated();
}
