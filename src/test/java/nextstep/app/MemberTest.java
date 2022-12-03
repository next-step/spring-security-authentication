package nextstep.app;

import nextstep.security.support.Authentication;
import nextstep.security.support.SecurityContextHolder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MemberTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void login_success() throws Exception {
        ResultActions loginResponse = requestAuthentication("Basic", "aHR0cCBiYXNpYw==");

        loginResponse.andExpect(status().isOk());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertThat(authentication.isAuthenticated()).isTrue();
    }

    @Test
    void login_fail_with_no_user() throws Exception {
        ResultActions response = requestAuthentication("none", "none");

        response.andExpect(status().isUnauthorized());
    }

    @Test
    void login_fail_with_invalid_password() throws Exception {
        ResultActions response = requestAuthentication("Basic", "invalid");

        response.andExpect(status().isUnauthorized());
    }

    private ResultActions requestAuthentication(final String type, final String credentials) throws Exception {
        return mockMvc.perform(get("/members")
                .header(HttpHeaders.AUTHORIZATION, String.format("%s %s", type, credentials))
                .contentType(MediaType.APPLICATION_JSON)
        );
    }
}
