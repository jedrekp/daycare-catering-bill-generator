import { Component, OnInit, Input } from '@angular/core';
import { Subject } from 'rxjs';
import { User } from '../user';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { ErrorHandlerService } from 'src/app/error/error-handler.service';
import { ERROR_HEADER } from 'src/app/const';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { UserDataService } from '../user-data.service';

@Component({
  selector: 'app-user-create-account',
  templateUrl: './user-create-account.component.html',
  styleUrls: ['./user-create-account.component.css']
})
export class UserCreateAccountComponent implements OnInit {

  @Input() private appUser: User
  private onClose: Subject<string>
  private header: string = ''
  private userForm: FormGroup

  constructor(
    private bsModalRef: BsModalRef,
    private userDataService: UserDataService,
    private dialogModalService: DialogModalService,
    private errorHandlerService: ErrorHandlerService
  ) { }

  ngOnInit() {
    if (this.appUser.daycareRole == 'HEADMASTER') {
      this.header = 'New headmaster account'
    } else if (this.appUser.daycareRole == 'GROUP_SUPERVISOR') {
      this.header = 'New group supervisor'
    }
    this.onClose = new Subject<string>()
    this.userForm = new FormGroup({
      firstName: new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]),
      lastName: new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]),
      username: new FormControl('', [Validators.required, Validators.minLength(5), Validators.maxLength(20)]),
      password: new FormControl('', [Validators.required, Validators.minLength(5), Validators.maxLength(20)]),
      passwordConfirm: new FormControl('', [Validators.required]),
      daycareRole: new FormControl('', [Validators.required])
    })
    this.userForm.patchValue({
      firstName: this.appUser.firstName,
      lastName: this.appUser.lastName,
      username: this.appUser.username,
      daycareRole: this.appUser.daycareRole
    })
  }

  onSubmit() {
    if (this.userForm.valid) {
      if (this.verifyIfPasswordConfirmedCorrectly()) {
        let userToSubmit = new User(
          this.appUser.id,
          this.userForm.get('firstName').value,
          this.userForm.get('lastName').value,
          this.userForm.get('username').value,
          this.userForm.get('password').value,
          this.userForm.get('daycareRole').value
        )
        this.userDataService.createNewUserAccount(userToSubmit).subscribe(
          user => {
            this.bsModalRef.hide()
            this.onClose.next(user.username)
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
    return this.userForm.get('password').value == this.userForm.get('passwordConfirm').value
  }

  onCancel() {
    this.bsModalRef.hide()
    this.onClose.next(null)
  }

}
