package nextstep.security.core.authentication.provider;

import nextstep.security.exception.AuthErrorCodes;
import nextstep.security.exception.AuthenticationException;

public class BasicTokenAuthenticationToken extends UsernamePasswordAuthenticationToken{


    public BasicTokenAuthenticationToken(String basic){
        super(null, null);
        try{
            if(basic.startsWith("Basic ")){
                this.setUsername(basic.replace("Basic ", "").split(":")[0]);
                this.setPassword(basic.replace("Basic ", "").split(":")[1]);
            }
        }catch (Exception e){
            throw new AuthenticationException(AuthErrorCodes.WRONG_BASIC_TOKEN_FORMAT);
        }

    }
}
