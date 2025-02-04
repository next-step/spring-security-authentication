package nextstep.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import nextstep.app.auth.MemberUserDetailsService;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.infrastructure.InmemoryMemberRepository;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.manager.AuthenticationManager;
import nextstep.security.authentication.manager.ProviderManager;
import nextstep.security.authentication.provider.UsernamePasswordAuthenticationProvider;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolderFilter;
import nextstep.security.filter.BasicAuthorizationFilter;
import nextstep.security.filter.FormAuthorizationFilter;
import nextstep.security.user.UserDetailsService;
import org.springframework.mock.web.MockFilterChain;

import java.util.List;

public final class MockFactory {
    public static final String USERNAME = "email@username.com";
    public static final String PASSWORD = "P@ssw0rD";

    private MockFactory() {}

    public static FilterChain createSecurityFilterChain() {
        final UserDetailsService userDetailsService = createUserDetailsService();
        final MockFilterChain filterChain = new MockFilterChain(
                createServlet(),
                new SecurityContextHolderFilter(),
                new BasicAuthorizationFilter(userDetailsService),
                new FormAuthorizationFilter(userDetailsService, "/login")
        );
        return filterChain;
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

    private static Servlet createServlet() {
        return new Servlet() {
            @Override
            public void init(ServletConfig servletConfig) {}

            @Override
            public ServletConfig getServletConfig() {
                return null;
            }

            @Override
            public void service(ServletRequest servletRequest, ServletResponse servletResponse) {}

            @Override
            public String getServletInfo() {
                return "";
            }

            @Override
            public void destroy() {}
        };
    }
}
