package nextstep.security.config;

import nextstep.security.UserDetails;

import java.security.Principal;

//  Implementations which use this class should be immutable.
public abstract class AbstractAuthenticationToken implements Authentication{

    private Object details;

    @Override
    public String getName() {
        if (this.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        if (this.getPrincipal() instanceof Principal principal) {
            return principal.getName();
        }
        return (this.getPrincipal() == null) ? "" : this.getPrincipal().toString();
    }

    public void setDetails(Object details) {
        this.details = details;
    }

    @Override
    public Object getDetails() {
        return this.details;
    }

}
