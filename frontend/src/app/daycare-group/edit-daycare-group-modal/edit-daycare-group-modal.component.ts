import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ConflictError } from 'src/app/common/error/conflict-error';
import { DaycareGroupDataService } from 'src/app/service/data/daycare-group-data.service';

@Component({
  selector: 'app-edit-daycare-group-modal',
  templateUrl: './edit-daycare-group-modal.component.html',
  styleUrls: ['./edit-daycare-group-modal.component.scss']
})
export class EditDaycareGroupModalComponent implements OnInit {

  editGroupForm: FormGroup

  constructor(
    public dialogRef: MatDialogRef<EditDaycareGroupModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private daycareGroupDataService: DaycareGroupDataService,
    private snackbar: MatSnackBar,
    fb: FormBuilder
  ) {
    this.editGroupForm = fb.group({
      groupName: fb.control(data.daycareGroup.groupName, [Validators.required, Validators.minLength(3), Validators.maxLength(20)])
    })
  }

  ngOnInit(): void {
  }

  get groupNameControl() {
    return this.editGroupForm.get('groupName')
  }

  get groupNameErrorMessage() {
    if (this.groupNameControl.hasError('required'))
      return 'Group name is required.'
    if (this.groupNameControl.hasError('minlength'))
      return 'Group name must be at least 3 characters long.'
    if (this.groupNameControl.hasError('maxlength'))
      return 'Group name cannot exceed 20 characters.'
  }

  onSubmit() {
    if (this.editGroupForm.valid) {
      this.daycareGroupDataService.update(this.editGroupForm.getRawValue(), this.data.daycareGroup.id).subscribe(
        daycareGroup => {
          this.dialogRef.close(daycareGroup.id)
          this.snackbar.open(`Daycare group has been updated.`)
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
