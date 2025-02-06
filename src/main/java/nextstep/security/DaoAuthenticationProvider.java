package nextstep.security;

import nextstep.security.domain.MemberDetail;
import nextstep.security.domain.MemberDetailService;
import nextstep.security.exception.AuthenticationException;

public class DaoAuthenticationProvider implements AuthenticationProvider {

    private final MemberDetailService memberDetailService;

    public DaoAuthenticationProvider(MemberDetailService memberDetailService) {
        this.memberDetailService = memberDetailService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (authentication.isAuthenticated()) {
            return authentication;
        }

        MemberDetail member = memberDetailService.findByUsername((String) authentication.getPrincipal());
        if (!member.isCorrectPassword((String) authentication.getCredentials())) {
            throw new AuthenticationException();
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member, null);
        authenticationToken.setAuthenticated(true);
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
