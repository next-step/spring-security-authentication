package nextstep.security.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

import nextstep.security.model.UserDetails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import static nextstep.security.utils.Constants.BASIC_TOKEN_PREFIX;

@Service
public class BasicAuthenticationService {

    public UserDetails mapTokenToUserDetails(String token) {
        if (Objects.isNull(token)) {
            return null;
        }

        String[] decodedToken =
                new String(Base64.getDecoder().decode(token.replace(BASIC_TOKEN_PREFIX, "")), StandardCharsets.UTF_8)
                        .split(":");

        if (decodedToken.length < 2 || StringUtils.isBlank(decodedToken[0]) || StringUtils.isBlank(decodedToken[1])) {
            return null;
        }

        return UserDetails.builder().userName(decodedToken[0]).password(decodedToken[1]).build();
    }

}
