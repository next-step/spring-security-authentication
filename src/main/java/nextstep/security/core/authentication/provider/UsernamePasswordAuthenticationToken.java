package nextstep.security.core.authentication.provider;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsernamePasswordAuthenticationToken {

    private String username;
    private String password;

}
