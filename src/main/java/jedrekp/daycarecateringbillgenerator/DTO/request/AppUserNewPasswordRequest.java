package jedrekp.daycarecateringbillgenerator.DTO.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class AppUserNewPasswordRequest {

    @NotNull
    @Length(min = 5, max = 20)
    private String currentPassword;

    @NotNull
    @Length(min = 5, max = 20)
    private String newPassword;
}
