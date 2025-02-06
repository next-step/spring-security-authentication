package nextstep.security.config;

import jakarta.servlet.Filter;
import nextstep.security.Fixture;
import nextstep.security.filter.BasicAuthenticationFilter;
import nextstep.security.filter.FormAuthenticationFilter;
import nextstep.security.user.UserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.security.Fixture.createBasicRequest;
import static nextstep.security.Fixture.createLoginRequest;
import static org.assertj.core.api.Assertions.assertThat;

class DefaultSecurityFilterChainTest {
    private List<Filter> filters;
    private SecurityFilterChain filterChain;

    @BeforeEach
    void setUp() {
        final UserDetailsService userDetailsService = Fixture.createUserDetailsService();
        filters = List.of(
                new BasicAuthenticationFilter(userDetailsService),
                new FormAuthenticationFilter(userDetailsService, "/login")
        );
        filterChain = new DefaultSecurityFilterChain(filters);
    }

    @DisplayName("getFilters 시, 등록한 것과 동일한 List 를 반환하는지 확인")
    @Test
    void getFilters() {
        assertThat(filterChain.getFilters())
                .isEqualTo(filters);
    }

    @DisplayName("BasicAuthorization 의 지원을 확인")
    @Test
    void basicAuth() {
        assertThat(filterChain.matches(createBasicRequest()))
                .isTrue();
    }

    @DisplayName("FormAuthorization 의 지원을 확인")
    @Test
    void formAuth() {
        assertThat(filterChain.matches(createLoginRequest()))
                .isTrue();
    }
}
