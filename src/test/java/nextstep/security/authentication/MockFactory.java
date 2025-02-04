package nextstep.security.authentication;

import nextstep.app.auth.MemberUserDetailsService;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.infrastructure.InmemoryMemberRepository;
import nextstep.security.authentication.manager.AuthenticationManager;
import nextstep.security.authentication.manager.ProviderManager;
import nextstep.security.authentication.provider.UsernamePasswordAuthenticationProvider;
import nextstep.security.user.UserDetailsService;

import java.util.List;

public final class MockFactory {
    public static final String USERNAME = "email@username.com";
    public static final String PASSWORD = "P@ssw0rD";

    private MockFactory() {}

    public static AuthenticationManager createProviderManager() {
        return new ProviderManager(List.of(
                createUsernamePasswordAuthenticationProvider()
        ));
    }

    public static UsernamePasswordAuthenticationProvider createUsernamePasswordAuthenticationProvider() {
        return new UsernamePasswordAuthenticationProvider(
                MockFactory.createUserDetailsService()
        );
    }

    private static UserDetailsService createUserDetailsService() {
        return new MemberUserDetailsService(createMemberRepository());
    }

    private static MemberRepository createMemberRepository() {
        final MemberRepository repository = new InmemoryMemberRepository();
        repository.save(createMember());
        return repository;
    }

    private static Member createMember() {
        return new Member(USERNAME, PASSWORD, "My Name", "My Image URL");
    }
}
