import { Component, OnInit } from '@angular/core';
import { DaycareGroup } from '../daycare-group';
import { DaycareGroupDataService } from '../daycare-group-data.service';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Router } from '@angular/router';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { CONFIRMATION_HEADER, ERROR_HEADER, ACTION_COMPLETED_HEADER } from 'src/app/const';
import { JwtAuthenticationService } from 'src/app/authentication/jwt-authentication.service';
import { ErrorHandlerService } from 'src/app/error/error-handler.service';

@Component({
  selector: 'app-daycare-group-list',
  templateUrl: './daycare-group-list.component.html',
  styleUrls: ['./daycare-group-list.component.css']
})
export class DaycareGroupListComponent implements OnInit {

  private modalRef: BsModalRef
  private daycareGroups: DaycareGroup[] = []

  constructor(
    private router: Router,
    private dialogModalService: DialogModalService,
    private daycareGroupDataService: DaycareGroupDataService,
    private authenticationService: JwtAuthenticationService,
    private errorHandlerService: ErrorHandlerService
  ) { }

  ngOnInit() {
    this.retrieveDaycareGroups()
  }

  retrieveDaycareGroups() {
    this.daycareGroupDataService.retrieveDaycareGroups().subscribe(
      daycareGroups => {
        this.daycareGroups = daycareGroups
      }, err => {
        this.errorHandlerService.redirectToErrorPage(err)
      })
  }

  deleteGroup(daycareGroup: DaycareGroup) {
    if (this.authenticationService.getUserRole() == 'HEADMASTER') {
      this.modalRef = this.dialogModalService.openConfirmationModal(CONFIRMATION_HEADER,
        `You are about to delete daycare group #${daycareGroup.id} ${daycareGroup.groupName}.\n
      Any children, that are currently assigned to it, will be left with no group.`)
      this.modalRef.content.onClose.subscribe(
        onClose => {
          if (onClose) {
            this.daycareGroupDataService.deleteDaycareGroup(daycareGroup.id).subscribe(
              response => {
                this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
                  `Daycare group #${daycareGroup.id} (${daycareGroup.groupName}) has been succesfully deleted.`)
                this.modalRef.content.onClose.subscribe(
                  onclose => {
                    this.retrieveDaycareGroups()
                  })
              },
              err => {
                this.dialogModalService.openInformationModal(ERROR_HEADER, this.errorHandlerService.getErrorMessage(err))
              })
          }
        })
    } else {
      this.dialogModalService.openInformationModal(ERROR_HEADER, 'You are not authorized to perform this aciton.')
    }
  }

}
