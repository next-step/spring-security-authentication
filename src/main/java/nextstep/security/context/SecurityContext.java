package nextstep.security.context;

import nextstep.security.authentication.Authentication;

public class SecurityContext {
    private final Authentication authentication;

    public SecurityContext(Authentication authentication) {
        this.authentication = authentication;
    }

    public static SecurityContext empty() {
        return SingletonHolder.EMPTY_INSTANCE;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    private static class SingletonHolder {
        private static final SecurityContext EMPTY_INSTANCE = new SecurityContext(
                new Authentication() {
                    @Override
                    public Object getPrincipal() {
                        return "";
                    }

                    @Override
                    public Object getCredentials() {
                        return "";
                    }
                }
        );
    }
}
