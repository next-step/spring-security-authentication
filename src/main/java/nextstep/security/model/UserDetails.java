package nextstep.security.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDetails {
    String userName;
    String password;
}
