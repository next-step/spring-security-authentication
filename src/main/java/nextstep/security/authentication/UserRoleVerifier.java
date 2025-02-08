package nextstep.security.authentication;

import nextstep.security.authentication.exception.MemberAccessDeniedException;
import nextstep.security.user.UsernamePasswordAuthenticationToken;

public class UserRoleVerifier {

    private static final String NORMAL_USER_ROLE = "NORMAL_USER";

    public void verify(UsernamePasswordAuthenticationToken authenticationToken) {
        validateHasAnyRole(authenticationToken);
        validateNotNormalUser(authenticationToken);
    }

    private void validateHasAnyRole(UsernamePasswordAuthenticationToken authenticationToken) {
        if (authenticationToken.getAuthorities() == null || authenticationToken.getAuthorities().isEmpty()) {
            throw new MemberAccessDeniedException();
        }
    }

    private void validateNotNormalUser(UsernamePasswordAuthenticationToken authenticationToken) {
        boolean isNormalUser = authenticationToken.getAuthorities().stream()
                .map(Object::toString)
                .anyMatch(NORMAL_USER_ROLE::equalsIgnoreCase);

        if (isNormalUser) {
            throw new MemberAccessDeniedException();
        }
    }
}
