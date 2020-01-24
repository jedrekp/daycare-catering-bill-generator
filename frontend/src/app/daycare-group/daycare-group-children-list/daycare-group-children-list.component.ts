import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { DaycareGroup } from '../daycare-group';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { DaycareGroupDataService } from '../daycare-group-data.service';
import { JwtAuthenticationService } from 'src/app/authentication/jwt-authentication.service';
import { ErrorHandlerService } from 'src/app/error/error-handler.service';
import { Child } from 'src/app/child/child';
import { CONFIRMATION_HEADER, ACTION_COMPLETED_HEADER, ERROR_HEADER } from 'src/app/const';

@Component({
  selector: 'app-daycare-group-children-list',
  templateUrl: './daycare-group-children-list.component.html',
  styleUrls: ['./daycare-group-children-list.component.css']
})
export class DaycareGroupChildrenListComponent implements OnInit {

  @Input() private daycareGroup: DaycareGroup
  @Output() private onChildrenChange: EventEmitter<number>
  private modalRef: BsModalRef

  constructor(
    private dialogModalService: DialogModalService,
    private daycareGroupDataService: DaycareGroupDataService,
    private authenticationService: JwtAuthenticationService,
    private errorHandlerService: ErrorHandlerService

  ) {
    this.onChildrenChange = new EventEmitter<number>()
  }

  ngOnInit() {
  }

  removeChildFromGroup(child: Child) {
    if (this.authenticationService.getUserRole() == 'HEADMASTER') {
      this.modalRef = this.dialogModalService.openConfirmationModal(CONFIRMATION_HEADER,
        `You are about to remove child #${child.id} (${child.firstName} ${child.lastName}) from daycare group #${this.daycareGroup.id}`)
      this.modalRef.content.onClose.subscribe(
        onclose => {
          if (onclose) {
            this.daycareGroupDataService.removeChildFromDaycareGroup(this.daycareGroup.id, child.id).subscribe(
              response => {
                this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
                  `Child #${child.id} (${child.firstName} ${child.lastName}) has been succesfully removed from daycare group #${this.daycareGroup.id}.`)
                this.modalRef.content.onClose.subscribe(
                  onclose => {
                    this.onChildrenChange.emit(this.daycareGroup.id)
                  })
              },
              err => {
                this.dialogModalService.openInformationModal(ERROR_HEADER, this.errorHandlerService.getErrorMessage(err))
              })
          }
        })
    } else {
      this.dialogModalService.openInformationModal(ERROR_HEADER, 'You are not authorized to perform this action.')
    }
  }

}
