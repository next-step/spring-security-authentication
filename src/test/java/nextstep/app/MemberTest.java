package nextstep.app;

import nextstep.app.domain.Member;
import nextstep.app.infrastructure.InmemoryMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.Base64Utils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class MemberTest {
    private static final Member TEST_MEMBER = InmemoryMemberRepository.TEST_MEMBER_1;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void members_success() throws Exception {
        ResultActions resultActions = requestMembers(TEST_MEMBER.getEmail(), TEST_MEMBER.getPassword());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].email").value("b@b.com"))
                .andExpect(jsonPath("$.[1].email").value("a@a.com"));
    }

    @Test
    void members_fail_with_invalid_password() throws Exception {
        ResultActions resultActions = requestMembers(TEST_MEMBER.getEmail(), "invalid");

        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    void login_fail_with_no_user() throws Exception {
        ResultActions response = requestMembers("none", "none");

        response.andExpect(status().isUnauthorized());
    }

    private ResultActions requestMembers(String email, String password) throws Exception {
        String encodeTarget = email + ":" + password;
        String encoded = Base64Utils.encodeToString(encodeTarget.getBytes());
        return mockMvc.perform(get("/members")
                .header("Authorization", "Basic " + encoded)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
        );
    }
}
