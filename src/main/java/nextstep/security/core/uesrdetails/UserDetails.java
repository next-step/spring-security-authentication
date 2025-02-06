package nextstep.security.core.uesrdetails;

import nextstep.security.role.GrantedAuthority;

import java.util.List;

public interface UserDetails {
    String getUsername();

    String getPassword();

    List<GrantedAuthority> getAuthorities();
}
