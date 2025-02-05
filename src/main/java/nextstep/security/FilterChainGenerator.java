package nextstep.security;

import jakarta.servlet.Filter;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.context.SecurityContextRepository;
import nextstep.security.context.SecurityContextRepositoryImpl;
import nextstep.security.filter.SecurityContextFilter;
import nextstep.security.filter.SecurityContextHolderFilter;
import nextstep.security.filter.UserNamePasswordAuthFilter;
import nextstep.security.filter.UserRoleFilter;
import nextstep.security.filter.config.SecurityFilterChain;

import java.util.LinkedList;
import java.util.List;

public class FilterChainGenerator {

    private final SecurityContextRepository securityContextRepository = new SecurityContextRepositoryImpl();
    private final AuthenticationManager authenticationManager;


    public FilterChainGenerator(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public SecurityFilterChain generate() {
        List<Filter> filters = new LinkedList<>();
        filters.add(new SecurityContextHolderFilter(securityContextRepository));
        filters.add(new SecurityContextFilter(authenticationManager, securityContextRepository));
        filters.add(new UserNamePasswordAuthFilter(authenticationManager, securityContextRepository));
        filters.add(new UserRoleFilter(securityContextRepository));

        return new UserNamePasswordSecurityFilterChain(filters);
    }
}
