package nextstep.security;

import nextstep.security.fixture.TestAuthentication;
import nextstep.security.fixture.TestAuthenticationProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


class ProviderManagerTest {

    @ParameterizedTest()
    @ValueSource(booleans = {true, false})
    @DisplayName("AuthenticationProvider 에 맞는 필터를 찾아 수행한다")
    void supportFilter(boolean support) {
        TestAuthentication testAuthentication = new TestAuthentication("principal", "credentials", false);
        AuthenticationProvider authenticationProvider = new TestAuthenticationProvider(support);
        ProviderManager providerManager = new ProviderManager(Set.of(authenticationProvider));

        Authentication result = providerManager.authenticate(testAuthentication);

        assertThat(result.isAuthenticated()).isEqualTo(support);
    }
}


