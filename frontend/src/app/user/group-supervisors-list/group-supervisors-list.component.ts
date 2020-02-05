import { Component, OnInit } from '@angular/core';
import { UserDataService } from '../user-data.service';
import { ErrorHandlerService } from 'src/app/error/error-handler.service';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { User } from '../user';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { CONFIRMATION_HEADER, ACTION_COMPLETED_HEADER, ERROR_HEADER } from 'src/app/const';

@Component({
  selector: 'app-group-supervisors-list',
  templateUrl: './group-supervisors-list.component.html',
  styleUrls: ['./group-supervisors-list.component.css']
})
export class GroupSupervisorsListComponent implements OnInit {

  private groupSupervisors: User[]
  private modalRef: BsModalRef

  constructor(
    private userDataService: UserDataService,
    private dialogModalService: DialogModalService,
    private errorHandlerService: ErrorHandlerService
  ) { }

  ngOnInit() {
    this.retrieveGroupSupervisors()
  }

  retrieveGroupSupervisors() {
    this.userDataService.getUsersByDaycareRole('GROUP_SUPERVISOR').subscribe(
      users => {
        this.groupSupervisors = users
      },
      err => {
        this.errorHandlerService.redirectToErrorPage(err)
      })
  }

  deleteGroupSupervisorAccount(groupSupervisor: User) {
    this.modalRef = this.dialogModalService.openConfirmationModal(CONFIRMATION_HEADER,
      `You are about to delete the account of user #${groupSupervisor.id} (${groupSupervisor.firstName} ${groupSupervisor.lastName}).`)
    this.modalRef.content.onClose.subscribe(
      onClose => {
        if (onClose) {
          this.userDataService.deleteUserAccount(groupSupervisor.id).subscribe(
            response => {
              this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
                `The account of user #${groupSupervisor.id} (${groupSupervisor.firstName} ${groupSupervisor.lastName}) has been succesfully deleted.`)
              this.modalRef.content.onClose.subscribe(
                onClose => {
                  this.retrieveGroupSupervisors()
                })
            },
            err => {
              this.dialogModalService.openInformationModal(ERROR_HEADER, this.errorHandlerService.getErrorMessage(err))
            })
        }
      })
  }

}
