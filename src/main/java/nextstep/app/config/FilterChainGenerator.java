package nextstep.app.config;

import nextstep.security.context.SecurityContextRepository;
import nextstep.security.context.SecurityContextRepositoryImpl;
import nextstep.security.UserNamePasswordSecurityFilterChain;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.filter.BasicAuthFilter;
import nextstep.security.filter.SecurityContextHolderFilter;
import nextstep.security.filter.UserNamePasswordAuthFilter;
import nextstep.security.filter.config.SecurityFilterChain;

import java.util.List;

public class FilterChainGenerator {

    private final SecurityContextRepository securityContextRepository = new SecurityContextRepositoryImpl();
    private final AuthenticationManager authenticationManager;


    public FilterChainGenerator(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public SecurityFilterChain generate() {
        return new UserNamePasswordSecurityFilterChain(
                List.of(
                        new SecurityContextHolderFilter(securityContextRepository),
                        new BasicAuthFilter(authenticationManager, securityContextRepository),
                        new UserNamePasswordAuthFilter(authenticationManager, securityContextRepository)
                )
        );
    }
}
