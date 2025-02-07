package nextstep.security.userdetails;

public interface UserDetailsService {
    UserDetails findUserDetailsByPrincipal(Object principal);

}
