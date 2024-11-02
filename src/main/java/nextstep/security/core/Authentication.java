package nextstep.security.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;


@Getter
@Setter
@AllArgsConstructor
public class Authentication {
    private Object principal;
    private Object credentials;
    private final ArrayList<String> authorities = new ArrayList<>();
    private boolean authenticated = false;
}
