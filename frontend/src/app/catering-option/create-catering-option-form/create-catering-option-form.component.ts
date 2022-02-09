import { Component, EventEmitter, OnInit, Output, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, FormGroupDirective, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ConflictError } from 'src/app/common/error/conflict-error';
import { CateringOptionDataService } from 'src/app/service/data/catering-option-data.service';

@Component({
  selector: 'app-create-catering-option-form',
  templateUrl: './create-catering-option-form.component.html',
  styleUrls: ['./create-catering-option-form.component.scss']
})
export class CreateCateringOptionFormComponent implements OnInit {

  @ViewChild(FormGroupDirective) formDirective: FormGroupDirective;

  @Output('optionCreated') optionCreated = new EventEmitter<number>()

  createOptionForm: FormGroup
  priceFormat = /^\d+(\.)?(\d{1,2})?$/


  constructor(
    private cateringOptionDataService: CateringOptionDataService,
    private snackbar: MatSnackBar,
    fb: FormBuilder
  ) {
    this.createOptionForm = fb.group({
      optionName: fb.control('', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]),
      dailyCost: fb.control('', [Validators.required, Validators.pattern(this.priceFormat), Validators.max(30)]),
      disabled: fb.control(false)
    })
  }

  ngOnInit(): void {
  }

  get optionNameControl() {
    return this.createOptionForm.get('optionName')
  }

  get dailyCostControl() {
    return this.createOptionForm.get('dailyCost')
  }

  get disabledControl() {
    return this.createOptionForm.get('disabled')
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
    if (this.createOptionForm.valid) {
      this.cateringOptionDataService.create(this.createOptionForm.getRawValue()).subscribe(
        option => {
          this.formDirective.resetForm()
          this.disabledControl.patchValue(false)
          this.snackbar.open(`New catering option has been created.`)
          this.optionCreated.emit(option.id)
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


