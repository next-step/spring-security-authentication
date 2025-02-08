package nextstep.security.authentication.provider;

import nextstep.app.domain.Base64PasswordEncoder;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.domain.UserDetailServiceImpl;
import nextstep.app.infrastructure.InmemoryMemberRepository;
import nextstep.security.authentication.Authentication;
import nextstep.security.user.UserDetailsService;
import nextstep.util.AuthenticationFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationProviderTest {

    private AuthenticationProvider authenticationProvider;
    private MemberRepository memberRepository;
    private UserDetailsService userDetailsService;

    private Authentication authentication;

    @BeforeEach
    void setUp() {
        this.memberRepository = new InmemoryMemberRepository();
        this.authenticationProvider = new DaoAuthenticationProvider(
                new UserDetailServiceImpl(memberRepository),
                new Base64PasswordEncoder()
        );

        this.userDetailsService = new UserDetailServiceImpl(memberRepository);
        this.authentication = AuthenticationFixture.createAuthentication();
        memberRepository.save(new Member("TEST", "TEST", "TEST", "TEST"));
    }

    @DisplayName("DaoAuthenticationProvider 인스턴스 생성 여부 테스트")
    @Test
    void createDaoAuthenticationProvider() {
        Authentication authenticate = authenticationProvider.authenticate(authentication);

        assertThat(authenticate.getPrincipal()).isEqualTo(authentication.getPrincipal());
    }

}
