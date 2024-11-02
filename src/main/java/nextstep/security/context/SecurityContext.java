package nextstep.security.context;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nextstep.security.core.Authentication;

@AllArgsConstructor
@Getter
@Setter
public class SecurityContext {
    private Authentication authentication;
}
