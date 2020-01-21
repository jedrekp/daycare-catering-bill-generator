package jedrekp.daycarecateringbillgenerator.utility;

import jedrekp.daycarecateringbillgenerator.entity.DaycareEmployee;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PreEncodedPasswordValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return DaycareEmployee.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        DaycareEmployee daycareEmployee = (DaycareEmployee) o;
        if (daycareEmployee.getPassword().length() < 5 || daycareEmployee.getPassword().length() > 20) {
            errors.rejectValue("password", "", "length must be between 5 and 20");
        }
    }
}
