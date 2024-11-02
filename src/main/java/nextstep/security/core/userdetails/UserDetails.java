package nextstep.security.core.userdetails;

import java.util.Collection;

public interface UserDetails {

    Collection<? extends String> getAuthorities();
    String getPassword();
    String getUsername();

}
