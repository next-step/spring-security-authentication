package nextstep.app.ui;

import nextstep.app.ui.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @PostMapping("/login")
    public ResponseEntity<Void> login(@ModelAttribute LoginRequest request) {
        return ResponseEntity.ok()
            .build();
    }
}
