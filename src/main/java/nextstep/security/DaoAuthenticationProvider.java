package nextstep.security;

import nextstep.security.domain.MemberDetail;
import nextstep.security.domain.MemberDetailService;
import nextstep.security.exception.AuthenticationException;

public class DaoAuthenticationProvider implements AuthenticationProvider {

    private final MemberDetailService memberDetailService;
    private final PasswordMatcher passwordMatcher;

    public DaoAuthenticationProvider(MemberDetailService memberDetailService, PasswordMatcher passwordMatcher) {
        this.memberDetailService = memberDetailService;
        this.passwordMatcher = passwordMatcher;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        if (authentication.isAuthenticated()) {
            return authentication;
        }

        MemberDetail member = memberDetailService.findByUsername((String) authentication.getPrincipal());
        if (passwordMatcher.matches(member.password(), (String) authentication.getCredentials())) {
            return new UsernamePasswordAuthenticationToken(member, null, true);
        }

        throw new AuthenticationException();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
