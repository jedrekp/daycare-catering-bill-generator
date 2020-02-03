import { Component, OnInit, Input } from '@angular/core';
import { Subject } from 'rxjs';
import { AppUser } from '../appUser';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { ErrorHandlerService } from 'src/app/error/error-handler.service';
import { ERROR_HEADER } from 'src/app/const';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { AppUserDataService } from '../app-user-data.service';

@Component({
  selector: 'app-app-user-account-create',
  templateUrl: './app-user-account-create.component.html',
  styleUrls: ['./app-user-account-create.component.css']
})
export class AppUserAccountCreateComponent implements OnInit {

  @Input() private appUser: AppUser
  private onClose: Subject<number>
  private header: string = ''
  private appUserForm: FormGroup

  constructor(
    private bsModalRef: BsModalRef,
    private appUserDataService: AppUserDataService,
    private dialogModalService: DialogModalService,
    private errorHandlerService: ErrorHandlerService
  ) { }

  ngOnInit() {
    if (this.appUser.daycareRole == 'HEADMASTER') {
      this.header = 'New headmaster account.'
    } else if (this.appUser.daycareRole == 'GROUP_SUPERVISOR') {
      this.header = 'New group supervisor account'
    }
    this.onClose = new Subject<number>()
    this.appUserForm = new FormGroup({
      firstName: new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]),
      lastName: new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]),
      username: new FormControl('', [Validators.required, Validators.minLength(5), Validators.maxLength(20)]),
      password: new FormControl('', [Validators.required, Validators.minLength(5), Validators.maxLength(20)]),
      passwordConfirm: new FormControl('', [Validators.required]),
      daycareRole: new FormControl('', [Validators.required])
    })
    this.appUserForm.patchValue({
      firstName: this.appUser.firstName,
      lastName: this.appUser.lastName,
      username: this.appUser.username,
      daycareRole: this.appUser.daycareRole
    })
  }

  onSubmit() {
    if (this.appUserForm.valid) {
      if (this.verifyIfPasswordConfirmedCorrectly()) {
        let appUserToSubmit = new AppUser(
          this.appUser.id,
          this.appUserForm.get('firstName').value,
          this.appUserForm.get('lastName').value,
          this.appUserForm.get('username').value,
          this.appUserForm.get('password').value,
          this.appUserForm.get('daycareRole').value
        )
        this.appUserDataService.createNewAppUserAccount(appUserToSubmit).subscribe(
          appUser => {
            this.bsModalRef.hide()
            this.onClose.next(appUser.id)
          }
          , err => {
            this.dialogModalService.openNestedInformationModal(ERROR_HEADER, this.errorHandlerService.getErrorMessage(err))
          })
      } else {
        this.dialogModalService.openNestedInformationModal(ERROR_HEADER, 'The password and confirmation password do not match.')
      }
    }
  }

  verifyIfPasswordConfirmedCorrectly() {
    return this.appUserForm.get('password').value == this.appUserForm.get('passwordConfirm').value
  }

  onCancel() {
    this.bsModalRef.hide()
    this.onClose.next(null)
  }

}
