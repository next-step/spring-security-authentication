package nextstep.security.user;

import java.util.Collection;

public interface UserDetails  {
    String username();
    String password();
    Collection<Object> authorities();
}
