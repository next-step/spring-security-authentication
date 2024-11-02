package nextstep.app.ui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextstep.app.domain.Member;
import nextstep.app.service.MemberService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members")
    public ResponseEntity<List<Member>> list(@RequestHeader(HttpHeaders.AUTHORIZATION)String authorization) {
        boolean loginResult = authenticate(authorization);
        if (!loginResult) {
            throw new AuthenticationException();
        }

        List<Member> members = memberService.findAll();
        return ResponseEntity.ok(members);
    }

    private boolean authenticate(String authorization) {
        if (StringUtils.isEmpty(authorization)) {
            return false;
        }

        if (!StringUtils.startsWith(authorization, "Basic ")) {
            return false;
        }

        String encodedCredentials = authorization.substring("Basic ".length());
        String decodedCredentials = new String(Base64Utils.decodeFromString(encodedCredentials), StandardCharsets.UTF_8);

        String[] userDetail = decodedCredentials.split(":",2);

        if (userDetail.length != 2) {
            return false;
        }

        Member loginMember = memberService.getMember(userDetail[0], userDetail[1]);
        if (loginMember == null) {
            return false;
        }

        log.info("Login success. ID : {} / password : {}", loginMember.getEmail(), loginMember.getPassword());
        return true;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handleAuthenticationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
