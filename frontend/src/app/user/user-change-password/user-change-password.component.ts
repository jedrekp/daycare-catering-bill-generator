import { Component, OnInit, Input } from '@angular/core';
import { UserDataService } from '../user-data.service';
import { ErrorHandlerService } from 'src/app/error/error-handler.service';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { Subject } from 'rxjs';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { ERROR_HEADER } from 'src/app/const';

@Component({
  selector: 'app-user-change-password',
  templateUrl: './user-change-password.component.html',
  styleUrls: ['./user-change-password.component.css']
})
export class UserChangePasswordComponent implements OnInit {

  @Input() username: string
  private onClose: Subject<boolean>
  private changePasswordForm: FormGroup

  constructor(
    private modalRef: BsModalRef,
    private userDataService: UserDataService,
    private dialogModalService: DialogModalService,
    private errorHandlerService: ErrorHandlerService,
  ) { }

  ngOnInit() {
    this.onClose = new Subject<boolean>()
    this.changePasswordForm = new FormGroup({
      currentPassword: new FormControl('', [Validators.required, Validators.minLength(5), Validators.maxLength(20)]),
      newPassword: new FormControl('', [Validators.required, Validators.minLength(5), Validators.maxLength(20)]),
      newPasswordConfirm: new FormControl('', Validators.required)
    })
  }

  onSubmit() {
    if (this.changePasswordForm.valid) {
      if (this.verifyIfPasswordConfirmedCorrectly()) {
        this.userDataService.changeUserPassword(
          this.username,
          this.changePasswordForm.get('currentPassword').value,
          this.changePasswordForm.get('newPassword').value)
          .subscribe(
            response => {
              this.modalRef.hide()
              this.onClose.next(true)
            },
            err => {
              this.dialogModalService.openNestedInformationModal(ERROR_HEADER, this.errorHandlerService.getErrorMessage(err))
            })
      } else {
        this.dialogModalService.openNestedInformationModal(ERROR_HEADER, 'The password and confirmation password do not match.')
      }
    }
  }

  onCancel() {
    this.modalRef.hide()
    this.onClose.next(false)
  }

  verifyIfPasswordConfirmedCorrectly() {
    return this.changePasswordForm.get('newPassword').value == this.changePasswordForm.get('newPasswordConfirm').value
  }

}
