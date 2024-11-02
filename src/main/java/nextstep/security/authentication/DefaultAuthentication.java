package nextstep.security.authentication;

import java.io.Serializable;

public class DefaultAuthentication implements Authentication, Serializable {

    private final String email;
    private final String password;
    private final boolean authenticated;

    private DefaultAuthentication(String email, String password, boolean authenticated) {
        this.email = email;
        this.password = password;
        this.authenticated = authenticated;
    }

    public static DefaultAuthentication unauthenticated(String email, String password) {
        return new DefaultAuthentication(email, password, false);
    }

    public static DefaultAuthentication authenticated(String email, String password) {
        return new DefaultAuthentication(email, password, true);
    }

    @Override
    public Object getPrincipal() {
        return email;
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }
}
