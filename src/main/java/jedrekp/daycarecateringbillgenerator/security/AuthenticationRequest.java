package jedrekp.daycarecateringbillgenerator.security;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
class AuthenticationRequest {

    @NotNull
    @Length(min = 5, max = 15)
    private String username;
    @NotNull
    @Length(min = 5, max = 15)
    private String password;

}
