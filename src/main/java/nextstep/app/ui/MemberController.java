package nextstep.app.ui;

import lombok.RequiredArgsConstructor;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.service.MemberService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members")
    public ResponseEntity<List<Member>> list(@RequestHeader(HttpHeaders.AUTHORIZATION)String authorization) {
        authenticate(authorization);

        List<Member> members = memberService.findAll();
        return ResponseEntity.ok(members);
    }

    private void authenticate(String authorization) {
        if (StringUtils.isEmpty(authorization)) {
            throw new AuthenticationException();
        }

        if (!StringUtils.startsWith(authorization, "Basic ")) {
            throw new AuthenticationException();
        }

        String encodedCredentials = authorization.substring("Basic ".length());
        String decodedCredentials = new String(Base64Utils.decodeFromString(encodedCredentials), StandardCharsets.UTF_8);

        String[] userDetail = decodedCredentials.split(":");

        if (userDetail.length != 2) {
            throw new AuthenticationException();
        }


    }

}
