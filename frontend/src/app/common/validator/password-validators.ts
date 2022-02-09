import { AbstractControl, FormControl, FormGroupDirective, NgForm, ValidationErrors, ValidatorFn } from "@angular/forms";
import { ErrorStateMatcher } from "@angular/material/core";

export class PasswordValidators {


    static passwordsMustMatch(control: AbstractControl): ValidationErrors | null {
        if (control.get('password').value != control.get('confirmPassword').value)
            return {
                passwordsMustMatch: true
            }
        return null
    }

}

export class ConfirmEqualPasswordsMatcher implements ErrorStateMatcher {
    isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
        return control.parent.hasError('passwordsMustMatch') && control.touched && control.parent.controls['password'].valid
    }
}