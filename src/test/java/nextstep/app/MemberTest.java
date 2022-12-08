package nextstep.app;

import nextstep.app.domain.Member;
import nextstep.app.infrastructure.InmemoryMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MemberTest {

    private static final Member TEST_MEMBER = InmemoryMemberRepository.TEST_MEMBER_1;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void get_members_success() throws Exception {
        final ResultActions membersResponse = requestMembersWith(TEST_MEMBER.getEmail(), TEST_MEMBER.getPassword());
        membersResponse.andExpect(status().isOk());
    }

    @Test
    void get_members_fail() throws Exception {
        final ResultActions membersResponse = requestMembersWith("none", "none");
        membersResponse.andExpect(status().isUnauthorized())
            .andExpect(header().stringValues(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=/login"));
    }

    @Test
    void get_members_fail_with_invalid_password() throws Exception {
        final ResultActions membersResponse = requestMembersWith(TEST_MEMBER.getEmail(), "invalid");
        membersResponse.andExpect(status().isUnauthorized())
            .andExpect(header().stringValues(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=/login"));
    }

    @Test
    void get_members_fail_with_invalid_auth_type() throws Exception {
        final ResultActions membersResponse = requestMembersWith("Basic1 ", TEST_MEMBER.getEmail(), TEST_MEMBER.getPassword());

        membersResponse.andExpect(status().isUnauthorized());
    }

    private ResultActions requestMembersWith(String username, String password) throws Exception {
        return requestMembersWith("Basic ", username, password);
    }

    private ResultActions requestMembersWith(String authType, String username, String password) throws Exception {
        final String usernameAndPassword = username + ":" + password;
        return mockMvc.perform(
            get("/members")
                .header(
                    HttpHeaders.AUTHORIZATION,
                    authType + Base64.getEncoder().encodeToString(usernameAndPassword.getBytes())
                )
        );
    }

}
