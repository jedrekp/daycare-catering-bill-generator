package jedrekp.daycarecateringbillgenerator.security;

import lombok.Data;

@Data
class AuthenticationRequest {

    private String username;
    private String password;

}
