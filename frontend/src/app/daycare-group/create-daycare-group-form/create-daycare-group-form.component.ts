import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { ConflictError } from 'src/app/common/error/conflict-error';
import { DaycareGroupDataService } from 'src/app/service/data/daycare-group-data.service';

@Component({
  selector: 'app-create-daycare-group-form',
  templateUrl: './create-daycare-group-form.component.html',
  styleUrls: ['./create-daycare-group-form.component.scss']
})
export class CreateDaycareGroupFormComponent implements OnInit {

  createGroupForm: FormGroup

  constructor(
    private daycareGroupDataService: DaycareGroupDataService,
    private snackbar: MatSnackBar,
    private router: Router,
    fb: FormBuilder
  ) {
    this.createGroupForm = fb.group({
      groupName: fb.control('', [Validators.required, Validators.minLength(3), Validators.maxLength(20)])
    })
  }

  ngOnInit(): void {
  }

  get groupNameControl() {
    return this.createGroupForm.get('groupName')
  }

  get groupNameErrorMessage() {
    if (this.groupNameControl.hasError('required'))
      return 'Group name is required.'
    if (this.groupNameControl.hasError('minlength'))
      return 'Group name must be at least 2 characters long.'
    if (this.groupNameControl.hasError('maxlength'))
      return 'Group name cannot exceed 20 characters.'
  }

  onSubmit() {
    if (this.createGroupForm.valid) {
      this.daycareGroupDataService.create(this.createGroupForm.getRawValue()).subscribe(
        daycareGroup => {
          this.router.navigate(['daycare-groups', daycareGroup.id])
          this.snackbar.open('New daycare group has been created.')
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
