package jedrekp.daycarecateringbillgenerator.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Getter
class Principal {

    private String username;
    private Collection<? extends GrantedAuthority> roles;

    Principal(String username, String role) {
        this.username = username;
        this.roles = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

}
