package nextstep.app.config;

import nextstep.app.domain.MemberRepository;
import nextstep.app.infrastructure.InmemoryMemberRepository;
import nextstep.app.service.MemberService;
import nextstep.security.core.userdetails.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

//@Configuration
//@EnableAspectJAutoProxy
public class AppConfig {

    @Bean
    UserDetailsService userDetailsService() {
        return new MemberService(memberRepository());
    }

    @Bean
    MemberRepository memberRepository() {
        return new InmemoryMemberRepository();
    }
}
