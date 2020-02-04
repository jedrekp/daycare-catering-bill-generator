import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { UserDataService } from '../user-data.service';
import { ErrorHandlerService } from 'src/app/error/error-handler.service';
import { ActivatedRoute } from '@angular/router';
import { JwtAuthenticationService } from 'src/app/authentication/jwt-authentication.service';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { UserAssignDaycareGroupComponent } from '../user-assign-daycare-group/user-assign-daycare-group.component';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { ACTION_COMPLETED_HEADER, ERROR_HEADER } from 'src/app/const';


@Component({
  selector: 'app-user-page',
  templateUrl: './user-page.component.html',
  styleUrls: ['./user-page.component.css']
})
export class UserPageComponent implements OnInit {

  private user: User;
  private modalRef: BsModalRef

  constructor(
    private route: ActivatedRoute,
    private userDataService: UserDataService,
    private authenticationService: JwtAuthenticationService,
    private bsModalService: BsModalService,
    private dialogModalService: DialogModalService,
    private errorHandlerService: ErrorHandlerService
  ) { }

  ngOnInit() {
    this.retrieveAppUser(this.route.snapshot.params['username'])
  }

  retrieveAppUser(username: string) {
    this.userDataService.getUserByUsername(username).subscribe(
      user => {
        this.user = user
      },
      err => this.errorHandlerService.redirectToErrorPage(err)
    )
  }

  checkIfAccountBelongsToLoggedUser() {
    return this.authenticationService.getUsername() == this.user.username
  }

  openAssignDaycareGroupToUserModal() {
    let initialState = { userId: this.user.id }
    this.modalRef = this.bsModalService.show(UserAssignDaycareGroupComponent,
      { class: 'modal-top-10 modal-sm', initialState, ignoreBackdropClick: true })
    this.modalRef.content.onClose.subscribe(
      user => {
        if (user) {
          this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
            `Daycare group #${user.daycareGroup.id} (${user.daycareGroup.groupName}) is now supervised
             by user #${user.id} (${user.firstName} ${user.lastName}).`)
          this.modalRef.content.onClose.subscribe(
            onClose => {
              this.retrieveAppUser(this.user.username)
            })
        }
      })
  }

  revokeDaycareGroupAssignment() {
    this.userDataService.removeDaycareGroupFromUser(this.user.id, this.user.daycareGroup.id).subscribe(
      response => {
        this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
          `Daycare group #${this.user.daycareGroup.id} (${this.user.daycareGroup.groupName}) is no longer supervised
          by user #${this.user.id} (${this.user.firstName} ${this.user.lastName}).`)
        this.modalRef.content.onClose.subscribe(
          onclose => {
            this.retrieveAppUser(this.user.username)
          })
      },
      err => {
        this.dialogModalService.openInformationModal(ERROR_HEADER, this.errorHandlerService.getErrorMessage(err))
      })
  }

}
