package nextstep.security.userdetails;

public interface UserDetails {
    Object getPrincipal();
    Object getCredentials();

    boolean match(Object credentials);
}
