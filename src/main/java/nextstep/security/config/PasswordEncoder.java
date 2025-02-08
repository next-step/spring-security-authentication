package nextstep.security.config;

public interface PasswordEncoder {

    String encode(CharSequence rawPassword);

    /**
     * @param rawPassword the raw password to encode and match
     * @param encodedPassword the encoded password from storage to compare with
     * @return true if the raw password, after encoding, matches the encoded password from storage
     */
    boolean matches(CharSequence rawPassword, String encodedPassword);

}
