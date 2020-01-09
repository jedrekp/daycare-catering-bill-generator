package jedrekp.daycarecateringbillgenerator.security;

import jedrekp.daycarecateringbillgenerator.entity.DaycareEmployee;
import jedrekp.daycarecateringbillgenerator.repository.DaycareEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final DaycareEmployeeRepository daycareEmployeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        DaycareEmployee daycareEmployee = daycareEmployeeRepository.findByAppUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found."));
        return buildUserForAuthentication(daycareEmployee);
    }

    private User buildUserForAuthentication(DaycareEmployee daycareEmployee) {
        List<GrantedAuthority> grantedAuthorities = Collections.singletonList(
                new SimpleGrantedAuthority(daycareEmployee.getDaycareJobPosition().toString()));
        return new User(
                daycareEmployee.getAppUsername(),
                daycareEmployee.getPassword(),
                true,
                true,
                true,
                true,
                grantedAuthorities);
    }
}
