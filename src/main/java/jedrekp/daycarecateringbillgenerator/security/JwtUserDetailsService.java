package jedrekp.daycarecateringbillgenerator.security;

import jedrekp.daycarecateringbillgenerator.entity.AppUser;
import jedrekp.daycarecateringbillgenerator.repository.AppUserRepository;
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

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found."));
        return buildUserForAuthentication(appUser);
    }

    private User buildUserForAuthentication(AppUser appUser) {
        List<GrantedAuthority> grantedAuthorities = Collections.singletonList(
                new SimpleGrantedAuthority(appUser.getDaycareRole().toString()));
        return new User(
                appUser.getUsername(),
                appUser.getPassword(),
                true,
                true,
                true,
                true,
                grantedAuthorities);
    }
}
