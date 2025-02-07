package nextstep.security.authentication;

import nextstep.security.role.GrantedAuthority;

import java.util.List;

public interface Authentication {
    String getPrincipal();

    String getCredentials();

    boolean isAuthenticated();

    List<GrantedAuthority> getAuthorities();

    void addAuthority(GrantedAuthority grantedAuthority);

    public boolean isNoPermission();
}
