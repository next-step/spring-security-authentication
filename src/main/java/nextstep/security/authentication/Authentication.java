package nextstep.security.authentication;

import java.util.Collection;

public interface Authentication {
    String getPrincipal();

    String getCredentials();

    boolean isAuthenticated();

    Collection<Role> getAuthorities();

    void addAuthority(Role role);

    public boolean isNoPermission();
}
