package nextstep.security.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nextstep.security.core.authority.GrantedAuthority;

import java.util.Collection;


@Getter
@Setter
@AllArgsConstructor
public class Authentication {
    private final Object principal;
    private Object credentials;
    private final Collection<GrantedAuthority> authorities;
    private boolean authenticated = false;

    public Authentication(Object principal, Object credentials, Collection<GrantedAuthority> authorities) {
        this.principal = principal;
        this.credentials = credentials;
        this.authorities = authorities;
    }
}
