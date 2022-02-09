import { Component, Inject, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { UnauthorizedError } from 'src/app/common/error/unauthorized-error';
import { ConfirmEqualPasswordsMatcher, PasswordValidators } from 'src/app/common/validator/password-validators';
import { UserDataService } from 'src/app/service/data/user-data.service';
import { User } from '../user';

@Component({
  selector: 'app-change-password-modal',
  templateUrl: './change-password-modal.component.html',
  styleUrls: ['./change-password-modal.component.scss']
})
export class ChangePasswordModalComponent implements OnInit {

  changePasswordForm: FormGroup
  confirmEqualPasswordsMatcher = new ConfirmEqualPasswordsMatcher()

  constructor(
    public dialogRef: MatDialogRef<ChangePasswordModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private userDataService: UserDataService,
    private snackbar: MatSnackBar,
    fb: FormBuilder
  ) {
    this.changePasswordForm = fb.group({
      currentPassword: fb.control('', [Validators.required, Validators.minLength(5), Validators.maxLength(20)]),
      password: fb.control('', [Validators.required, Validators.minLength(5), Validators.maxLength(20)]),
      confirmPassword: fb.control(''),
    },
      { validators: [PasswordValidators.passwordsMustMatch] }
    )
  }

  ngOnInit() {
  }

  get currentPasswordControl() {
    return this.changePasswordForm.get('currentPassword')
  }

  get passwordControl() {
    return this.changePasswordForm.get('password')
  }

  get confirmPassword() {
    return this.changePasswordForm.get('confirmPassword')
  }

  get currentPasswordErrorMessage() {
    if (this.currentPasswordControl.hasError('required'))
      return 'You need to provide your current password.'
    if (this.currentPasswordControl.hasError('minlength'))
      return 'Password must be at least 5 characters long.'
    if (this.currentPasswordControl.hasError('maxlength'))
      return 'Password cannot exceed 20 characters.'
  }

  get passwordErrorMessage() {
    if (this.passwordControl.hasError('required'))
      return 'New password is required.'
    if (this.passwordControl.hasError('minlength'))
      return 'New password must be at least 5 characters long.'
    if (this.passwordControl.hasError('maxlength'))
      return 'New password cannot exceed 20 characters.'
  }

  get confirmPasswordErrorMessage() {
    if (this.changePasswordForm.hasError('passwordsMustMatch'))
      return 'Passwords do not match.'
  }

  onSubmit() {
    if (this.changePasswordForm.valid) {
      const passwordRequest = {
        currentPassword: this.currentPasswordControl.value,
        newPassword: this.passwordControl.value
      }
      this.userDataService.changeUserPassword(passwordRequest, this.data.user.username).subscribe(
        () => {
          this.snackbar.open('Your password has been succefully updated.')
          this.dialogRef.close(this.data.username)
        },
        err => {
          if (err instanceof UnauthorizedError)
            this.snackbar.open(err.originalError?.error, null, { panelClass: 'dcbg-error-snackbar' })
          else {
            this.dialogRef.close()
            throw err
          }
        })
    }
  }

}