package nextstep.security.util.string;

import nextstep.security.util.Pair;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ColonSeparatedParserTest {

    @Test
    @DisplayName(" *:* 형식의 문자열을 파싱한다")
    void successTest() {
        var result = ColonSeparatedParser.parse("abc:password");
        Assertions.assertThat(result).isEqualTo(Pair.of("abc", "password"));
    }

    @ParameterizedTest
    @DisplayName(" *:* 이 아닌 문자열은 null을 반환한다")
    @ValueSource(strings = {"str", "::", "a::b", "a:b:c"})
    void failTest(String str) {
        var result = ColonSeparatedParser.parse(str);
        Assertions.assertThat(result).isNull();

    }

}
