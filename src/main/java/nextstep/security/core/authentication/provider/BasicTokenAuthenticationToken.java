package nextstep.security.core.authentication.provider;

import nextstep.security.exception.AuthErrorCodes;
import nextstep.security.exception.AuthenticationException;

import java.util.Arrays;
import java.util.Base64;

public class BasicTokenAuthenticationToken extends UsernamePasswordAuthenticationToken{


    public BasicTokenAuthenticationToken(String basic){
        super(null, null);
        try{
            if(basic.startsWith("Basic ")){
                String base64 = basic.replace("Basic ", "");
                String original = new String(Base64.getDecoder().decode(base64));
                this.setUsername(original.split(":")[0]);
                this.setPassword(original.split(":")[1]);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new AuthenticationException(AuthErrorCodes.WRONG_BASIC_TOKEN_FORMAT);
        }

    }
}
