package nextstep.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.app.auth.MemberUserDetailsService;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.infrastructure.InmemoryMemberRepository;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.manager.AuthenticationManager;
import nextstep.security.authentication.manager.ProviderManager;
import nextstep.security.authentication.provider.UsernamePasswordAuthenticationProvider;
import nextstep.security.config.VirtualFilterChain;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolderFilter;
import nextstep.security.filter.BasicAuthorizationFilter;
import nextstep.security.filter.FormAuthorizationFilter;
import nextstep.security.user.UserDetailsService;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Base64;
import java.util.List;

public final class MockFactory {
    public static final String USERNAME = "email@username.com";
    public static final String PASSWORD = "P@ssw0rD";

    private MockFactory() {}

    public static FilterChain createFilterChain() {
        final UserDetailsService userDetailsService = createUserDetailsService();
        return new VirtualFilterChain(new MockFilterChain(), List.of(
                new SecurityContextHolderFilter(),
                new BasicAuthorizationFilter(userDetailsService),
                new FormAuthorizationFilter(userDetailsService, "/login")
        ));
    }

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

    public static UserDetailsService createUserDetailsService() {
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

    public static HttpServletRequest createBasicRequest() {
        final String authorization = "Basic " + Base64.getEncoder().encodeToString(
                (USERNAME + ":" + PASSWORD).getBytes()
        );
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", authorization);
        request.setRequestURI("/members");
        return request;
    }

    public static HttpServletRequest createLoginRequest() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/login");
        request.setParameter("username", USERNAME);
        request.setParameter("password", PASSWORD);
        request.setContentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        return request;
    }

    public static SecurityContext createSecurityContext(String username, String password) {
        return new SecurityContext(createAuthentication(username, password));
    }

    public static Authentication createAuthentication(String username, String password) {
        return new Authentication() {
            @Override
            public Object getPrincipal() {
                return username;
            }

            @Override
            public Object getCredentials() {
                return password;
            }
        };
    }
}
