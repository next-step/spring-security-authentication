package nextstep.security.util.string;

import jakarta.annotation.Nullable;
import nextstep.security.util.Pair;

public class ColonSeparatedParser {

    public static final String COLON = ":";

    @Nullable
    public static Pair<String, String> parse(final String str) {
        var split = str.split(COLON);
        if (split.length != 2) {
            return null;
        }
        return Pair.of(split[0], split[1]);
    }

}
