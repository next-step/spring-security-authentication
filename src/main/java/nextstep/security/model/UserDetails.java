package nextstep.security.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDetails implements Serializable {
    String userName;
    String password;
}
