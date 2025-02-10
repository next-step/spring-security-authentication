package nextstep.security.config;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.AuthenticationException;
import nextstep.security.util.Base64Convertor;

public class BasicAuthenticationToken extends AbstractAuthenticationToken {

    private final String principal;
    private final String credentials;
    private boolean authenticated = false;


    public BasicAuthenticationToken(String authorizationHeader) {
        String authType = authorizationHeader.split(" ")[0];
        String credentials = authorizationHeader.split(" ")[1];
        String decodedString = Base64Convertor.decode(credentials);

        checkAuthType(authType);
        String[] usernameAndPassword = decodedString.split(":");
        this.principal = usernameAndPassword[0];
        this.credentials = usernameAndPassword[1];
    }

    public BasicAuthenticationToken(String principal, String credentials) {
        this.principal = principal;
        this.credentials = credentials;
    }

    private void checkAuthType(String authType) {
        if (!HttpServletRequest.BASIC_AUTH.equalsIgnoreCase(authType)) {
            throw new AuthenticationException();
        }
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

}
