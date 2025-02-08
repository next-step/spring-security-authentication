package nextstep.security.authentication.provider;

import nextstep.app.domain.Base64PasswordEncoder;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.domain.UserDetailServiceImpl;
import nextstep.app.infrastructure.InmemoryMemberRepository;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.exception.AuthenticationException;
import nextstep.util.AuthenticationFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class AuthenticationManagerTest {

    private ProviderManager providerManager;
    private Authentication authentication;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        this.memberRepository = new InmemoryMemberRepository();
        this.providerManager = new ProviderManager(
                List.of(
                        new DaoAuthenticationProvider(
                                new UserDetailServiceImpl(memberRepository),
                                new Base64PasswordEncoder()
                        )
                )
        );
        this.authentication = AuthenticationFixture.createAuthentication();

        memberRepository.save(new Member("TEST", "TEST", "TEST", "TEST"));
    }

    @DisplayName("인증가능한 Provider가 없을 경우 예외를 발생시킨다.")
    @Test
    void authenticationException() {
        Authentication emptyAuthentication = new UsernamePasswordAuthenticationToken("EMPTY", "EMPTY");

        assertThatThrownBy(() -> providerManager.authentication(emptyAuthentication))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @DisplayName("인증가능한 회원일 경우 인증을 성공한다.")
    @Test
    void setAuthentication() {
        Authentication authenticate = providerManager.authentication(authentication);
        assertThat(authenticate).isNotNull();
    }
}
