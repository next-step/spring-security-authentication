package nextstep.security.model;

public interface SecurityAuthentication {
    // Collection<? extends GrantedAuthority> getAuthorities();
    Object getCredentials();
    Object getPrincipal();
    boolean isAuthenticated();
}
