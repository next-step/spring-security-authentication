package nextstep.security.config;

public interface SecurityContext {

    Authentication getAuthentication();

    void setAuthentication(Authentication authentication);

}
