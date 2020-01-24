package jedrekp.daycarecateringbillgenerator.utility;

import jedrekp.daycarecateringbillgenerator.entity.AppUser;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PreEncodedPasswordValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return AppUser.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        AppUser appUser = (AppUser) o;
        if (appUser.getPassword().length() < 5 || appUser.getPassword().length() > 20) {
            errors.rejectValue("password", "", "length must be between 5 and 20");
        }
    }
}
