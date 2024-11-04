package nextstep.security.provider;

public interface AuthenticationManager {

    AuthenticationProvider getProvider(String principal);
}
