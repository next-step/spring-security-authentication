package nextstep.app;

import java.util.Base64;
import java.util.stream.Stream;

import nextstep.security.model.UserDetails;
import nextstep.security.service.BasicAuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BasicAuthenticationServiceTest {

    @InjectMocks
    BasicAuthenticationService underTest;

    static Stream<Arguments> tokenProvider() {
        return Stream.of(
                Arguments.of("Basic invalid"),
                null,
                Arguments.of(""),
                Arguments.of("Basic ")
        );
    }

    @ParameterizedTest
    @MethodSource("tokenProvider")
    void mapTokenToUserDetails_shouldReturnNullWhenTokenIsCorrupted(String token) {
        // arrange
        // act
        var result = underTest.mapTokenToUserDetails(token);

        // assert
        assertThat(result).isNull();
    }

    @Test
    void mapTokenToUserDetails_shouldReturnUserDetailsWithMatchedResult() {
        // arrange
        var email = "test_email";
        var password = "test_password";
        var token = "Basic " + Base64.getEncoder().encodeToString((email+":"+password).getBytes());

        // act
        var result = underTest.mapTokenToUserDetails(token);

        // assert
        assertThat(result).isNotNull().isInstanceOf(UserDetails.class);
        assertThat(result.getUserName()).isEqualTo(email);
        assertThat(result.getPassword()).isEqualTo(password);
    }

}
