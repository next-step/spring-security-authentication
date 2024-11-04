package nextstep.security.provider;

import lombok.RequiredArgsConstructor;
import nextstep.security.credential.SecurityCredential;
import nextstep.security.credential.UsernamePasswordCredential;
import nextstep.security.model.SecurityAuthentication;
import nextstep.security.model.UserDetails;
import nextstep.security.service.UserDetailService;

@RequiredArgsConstructor
public class UsernameProvider implements AuthenticationProvider {
    private final UserDetailService userDetailService;

    @Override
    public SecurityAuthentication authenticate(SecurityCredential credential) {
        UsernamePasswordCredential usernamePasswordCredential = (UsernamePasswordCredential) credential;

        UserDetails userDetails = userDetailService.getUserDetails(usernamePasswordCredential.getUsername(), usernamePasswordCredential.getPassword());

        if (userDetails == null) {
            return new SecurityAuthentication(null, false);
        }

        return new SecurityAuthentication(userDetails, true);
    }
}
