package jedrekp.daycarecateringbillgenerator.security;

import lombok.Data;

@Data
class AuthenticationResponse {
    private final String jwtToken;
}
