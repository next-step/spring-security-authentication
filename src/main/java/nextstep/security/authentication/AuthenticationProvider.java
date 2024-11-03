package nextstep.security.authentication;

import nextstep.app.infrastructure.InmemoryMemberRepository;
import nextstep.app.service.MemberService;
import nextstep.security.core.Authentication;
import nextstep.security.core.AuthenticationException;
import nextstep.security.core.userdetails.UserDetails;
import nextstep.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.Objects;

//@Configurable
public class AuthenticationProvider {

//    @Autowired
    private UserDetailsService userDetailsService =  new MemberService(new InmemoryMemberRepository());


    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 사용자 정보 load
        UserDetails loadedUser = retrieveUser((String) authentication.getPrincipal());

        // 패스워드 체크
        authenticationChecks(authentication, loadedUser);

        // 인증 정보 생성
        return createSuccessAuthentication(loadedUser);
    }

    private Authentication createSuccessAuthentication(UserDetails user) {
        Authentication authentication = new Authentication(user, null, user.getAuthorities());
        authentication.setAuthenticated(true);
        return authentication;
    }

    private void authenticationChecks(Authentication authentication, UserDetails loadedUser) {
        System.out.println("authentication = " + authentication.getCredentials());
        System.out.println("loadedUser = " + loadedUser.getPassword());
        if (!Objects.equals(authentication.getCredentials().toString(), loadedUser.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }
    }

    private UserDetails retrieveUser(String username) {
        return userDetailsService.loadUserByUsername(username);
    }
}
