package nextstep.app;

import java.util.Base64;
import java.util.stream.Stream;

import nextstep.app.domain.Member;
import nextstep.app.service.auth.BasicAuthenticationService;
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
    void decodeToken_shouldReturnNullWhenTokenIsCorrupted(String token) {
        // arrange
        // act
        var result = underTest.mapTokenToMember(token);

        // assert
        assertThat(result).isNull();
    }

    @Test
    void decodeToken_shouldReturnMemberWith() {
        // arrange
        var email = "test_email";
        var password = "test_password";
        var token = "Basic " + Base64.getEncoder().encodeToString((email+":"+password).getBytes());

        // act
        var result = underTest.mapTokenToMember(token);

        // assert
        assertThat(result).isNotNull().isInstanceOf(Member.class);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getPassword()).isEqualTo(password);
        assertThat(result.getName()).isNull();
        assertThat(result.getImageUrl()).isNull();
    }

}
