package jedrekp.daycarecateringbillgenerator.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class AppUserNewPasswordRequest {

    @NotNull
    @Length(min = 5, max = 20)
    private String currentPassword;

    @NotNull
    @Length(min = 5, max = 20)
    private String newPassword;
}
