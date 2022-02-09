import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ConflictError } from 'src/app/common/error/conflict-error';
import { CateringOptionDataService } from 'src/app/service/data/catering-option-data.service';

@Component({
  selector: 'app-edit-catering-option-modal',
  templateUrl: './edit-catering-option-modal.component.html',
  styleUrls: ['./edit-catering-option-modal.component.scss']
})
export class EditCateringOptionModalComponent implements OnInit {


  editOptionForm: FormGroup
  priceFormat = /^\d+(\.)?(\d{1,2})?$/


  constructor(
    public dialogRef: MatDialogRef<EditCateringOptionModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private cateringOptionDataService: CateringOptionDataService,
    private snackbar: MatSnackBar,
    fb: FormBuilder
  ) {
    this.editOptionForm = fb.group({
      optionName: fb.control(data.cateringOption.optionName, [Validators.required, Validators.minLength(3), Validators.maxLength(20)]),
      dailyCost: fb.control(data.cateringOption.dailyCost, [Validators.required, Validators.pattern(this.priceFormat), Validators.max(30)]),
      disabled: fb.control(data.cateringOption.disabled)
    })
  }

  ngOnInit(): void {
  }

  get optionNameControl() {
    return this.editOptionForm.get('optionName')
  }

  get dailyCostControl() {
    return this.editOptionForm.get('dailyCost')
  }

  get optionNameErrorMessage() {
    if (this.optionNameControl.hasError('required'))
      return 'Catering option name is required.'
    if (this.optionNameControl.hasError('minlength'))
      return 'Catering option name must be at least 3 characters long.'
    if (this.optionNameControl.hasError('maxlength'))
      return 'Catering option name cannot exceed 20 characters.'
  }

  get dailyCostErrorMessage() {
    if (this.dailyCostControl.hasError('required') || this.dailyCostControl.hasError('pattern'))
      return 'Daily cost is missing or price format is invalid.'
    if (this.dailyCostControl.hasError('max'))
      return 'Daily cost cannot exceed 30 PLN.'

  }

  onSubmit() {
    if (this.editOptionForm.valid) {
      this.cateringOptionDataService.update(this.editOptionForm.getRawValue(), this.data.cateringOption.id).subscribe(
        option => {
          this.dialogRef.close(option.id)
          this.snackbar.open(`Catering option #${option.id} has been updated.`)
        },
        err => {
          if (err instanceof ConflictError)
            this.snackbar.open(err.originalError?.error, null, { panelClass: 'dcbg-error-snackbar' })
          else {
            this.dialogRef.close()
            throw err
          }
        })
    }
  }

}