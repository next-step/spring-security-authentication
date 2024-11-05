package nextstep.security.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nextstep.security.model.SecurityAuthentication;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SecurityContext implements Serializable {
    private SecurityAuthentication securityAuthentication;
}
