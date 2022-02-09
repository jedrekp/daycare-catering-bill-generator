import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ConflictError } from 'src/app/common/error/conflict-error';
import { ChildDataService } from 'src/app/service/data/child-data.service';

@Component({
  selector: 'app-edit-child-modal',
  templateUrl: './edit-child-modal.component.html',
  styleUrls: ['./edit-child-modal.component.scss']
})
export class EditChildModalComponent implements OnInit {

  editChildForm: FormGroup

  constructor(
    public dialogRef: MatDialogRef<EditChildModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private childDataService: ChildDataService,
    private snackbar: MatSnackBar,
    fb: FormBuilder
  ) {
    this.editChildForm = fb.group({
      childId: fb.control(data.child.id),
      firstName: fb.control(data.child.firstName, [Validators.required, Validators.minLength(2), Validators.maxLength(20)]),
      lastName: fb.control(data.child.lastName, [Validators.required, Validators.minLength(2), Validators.maxLength(20)]),
      parentEmail: fb.control(data.child.parentEmail, [Validators.required, Validators.email]),
      archived: fb.control(data.child.archived)
    })
  }

  ngOnInit(): void {

  }

  get firstNameControl() {
    return this.editChildForm.get('firstName')
  }

  get lastNameControl() {
    return this.editChildForm.get('lastName')
  }

  get parentEmailControl() {
    return this.editChildForm.get('parentEmail')
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
    if (this.editChildForm.valid) {
      this.childDataService.update(this.editChildForm.getRawValue(), this.data.child.id).subscribe(
        child => {
          this.dialogRef.close(child.id)
          this.snackbar.open(`Child profile has been updated.`)
        },
        err => {
          if (err instanceof ConflictError) {
            this.snackbar.open(err.originalError?.error, null, { panelClass: 'dcbg-error-snackbar' })
          }
          else {
            this.dialogRef.close()
            throw err
          }
        })
    }
  }

}
