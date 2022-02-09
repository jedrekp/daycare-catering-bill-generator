import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { ConflictError } from 'src/app/common/error/conflict-error';
import { ChildDataService } from 'src/app/service/data/child-data.service';

@Component({
  selector: 'app-create-child-form',
  templateUrl: './create-child-form.component.html',
  styleUrls: ['./create-child-form.component.scss']
})
export class CreateChildFormComponent implements OnInit {

  createChildForm: FormGroup

  constructor(
    private childDataService: ChildDataService,
    private snackbar: MatSnackBar,
    private router: Router,
    fb: FormBuilder
  ) {
    this.createChildForm = fb.group({
      firstName: fb.control('', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]),
      lastName: fb.control('', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]),
      parentEmail: fb.control('', [Validators.required, Validators.email]),
      archived: fb.control(false)
    })
  }

  ngOnInit(): void {
  }

  get firstNameControl() {
    return this.createChildForm.get('firstName')
  }

  get lastNameControl() {
    return this.createChildForm.get('lastName')
  }

  get parentEmailControl() {
    return this.createChildForm.get('parentEmail')
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

  get parentEmailErrorMessage() {
    if (this.parentEmailControl.hasError('required'))
      return 'Parent email is required.'
    if (this.parentEmailControl.hasError('email'))
      return 'Invalid email format.'
  }

  onSubmit() {
    if (this.createChildForm.valid) {
      this.childDataService.create(this.createChildForm.getRawValue()).subscribe(
        child => {
          this.router.navigate(['child-profiles', child.id])
          this.snackbar.open(`New child profile has been created.`)
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


