package nextstep.security.core;

public enum SecurityPrincipal {

    USERNAME_PASSWORD_AUTHENTICATION("username password auth"),
    BASIC_TOKEN_AUTHENTICATION("basic token auth");
    ;

    final String expression;

    SecurityPrincipal(String expression) {
        this.expression = expression;
    }
}
