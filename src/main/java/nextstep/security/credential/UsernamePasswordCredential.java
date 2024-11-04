package nextstep.security.credential;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsernamePasswordCredential implements SecurityCredential {
    private String username;
    private String password;
}
