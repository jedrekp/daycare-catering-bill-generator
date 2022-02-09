import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { ConflictError } from 'src/app/common/error/conflict-error';
import { ConfirmEqualPasswordsMatcher, PasswordValidators } from 'src/app/common/validator/password-validators';
import { UserDataService } from 'src/app/service/data/user-data.service';
import { GROUP_SUPERVISOR } from 'src/const';


@Component({
  selector: 'app-create-group-supervisor-form',
  templateUrl: './create-group-supervisor-form.component.html',
  styleUrls: ['./create-group-supervisor-form.component.scss']
})
export class CreateGroupSupervisorFormComponent implements OnInit {

  createSupervisorForm: FormGroup
  confirmEqualPasswordsMatcher = new ConfirmEqualPasswordsMatcher()

  constructor(
    private userDataService: UserDataService,
    private snackbar: MatSnackBar,
    private router: Router,
    fb: FormBuilder
  ) {
    this.createSupervisorForm = fb.group({
      firstName: fb.control('', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]),
      lastName: fb.control('', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]),
      username: fb.control('', [Validators.required, Validators.minLength(5), Validators.maxLength(20)]),
      password: fb.control('', [Validators.required, Validators.minLength(5), Validators.maxLength(20)]),
      confirmPassword: fb.control(''),
      daycareRole: fb.control(GROUP_SUPERVISOR),
    },
      { validators: [PasswordValidators.passwordsMustMatch] }
    )
  }

  ngOnInit(): void {
  }

  get firstNameControl() {
    return this.createSupervisorForm.get('firstName')
  }

  get lastNameControl() {
    return this.createSupervisorForm.get('lastName')
  }

  get usernameControl() {
    return this.createSupervisorForm.get('username')
  }

  get passwordControl() {
    return this.createSupervisorForm.get('password')
  }

  get confirmPasswordControl() {
    return this.createSupervisorForm.get('confirmPassword')
  }

  get firstNameErrorMessage() {
    if (this.firstNameControl.hasError('required'))
      return 'First name is required.'
    if (this.firstNameControl.hasError('minlength'))
      return 'First name must be at least 2 characters long.'
    if (this.firstNameControl.hasError('maxlength'))
      return 'First name cannot exceed 20 characters.'
  }

  get lastNameErrorMessage() {
    if (this.lastNameControl.hasError('required'))
      return 'Last name is required.'
    if (this.lastNameControl.hasError('minlength'))
      return 'Last name must be at least 2 characters long.'
    if (this.lastNameControl.hasError('maxlength'))
      return 'Last name cannot exceed 20 characters.'
  }

  get usernameErrorMessage() {
    if (this.usernameControl.hasError('required'))
      return 'Username is required.'
    if (this.usernameControl.hasError('minlength'))
      return 'Username must be at least 5 characters long.'
    if (this.usernameControl.hasError('maxlength'))
      return 'Username cannot exceed 20 characters.'
  }

  get passwordErrorMessage() {
    if (this.passwordControl.hasError('required'))
      return 'Password is required.'
    if (this.passwordControl.hasError('minlength'))
      return 'Password must be at least 5 characters long.'
    if (this.passwordControl.hasError('maxlength'))
      return 'Password cannot exceed 20 characters.'
  }

  get confirmPasswordErrorMessage() {
    if (this.createSupervisorForm.hasError('passwordsMustMatch'))
      return 'Passwords do not match.'
  }



  onSubmit() {
    if (this.createSupervisorForm.valid) {
      const supervisor = this.createSupervisorForm.getRawValue()
      delete supervisor.confirmPassword
      this.userDataService.create(supervisor).subscribe(
        supervisor => {
          this.router.navigate(['group-supervisors', supervisor.username])
          this.snackbar.open(`New group supervisor profile has been created.`)
        },
        err => {
          if (err instanceof ConflictError)
            this.snackbar.open(err.originalError?.error, null, { panelClass: 'dcbg-error-snackbar' })
          else
            throw err
        })
    }
  }

}
