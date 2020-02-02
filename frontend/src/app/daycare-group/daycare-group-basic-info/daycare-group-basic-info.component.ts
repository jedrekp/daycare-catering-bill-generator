import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { DaycareGroup } from '../daycare-group';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { DaycareGroupDataService } from '../daycare-group-data.service';
import { JwtAuthenticationService } from 'src/app/authentication/jwt-authentication.service';
import { ErrorHandlerService } from 'src/app/error/error-handler.service';
import { DaycareGroupCreateEditComponent } from '../daycare-group-create-edit/daycare-group-create-edit.component';
import { ACTION_COMPLETED_HEADER, ERROR_HEADER, CONFIRMATION_HEADER } from 'src/app/const';
import { Router } from '@angular/router';

@Component({
  selector: 'app-daycare-group-basic-info',
  templateUrl: './daycare-group-basic-info.component.html',
  styleUrls: ['./daycare-group-basic-info.component.css']
})
export class DaycareGroupBasicInfoComponent implements OnInit {

  @Input() private daycareGroup: DaycareGroup
  @Output() private onBasicInfoChange: EventEmitter<number>
  private modalRef: BsModalRef

  constructor(
    private router: Router,
    private bsModalService: BsModalService,
    private dialogModalService: DialogModalService,
    private daycareGroupDataService: DaycareGroupDataService,
    private authenticationService: JwtAuthenticationService,
    private errorHandlerService: ErrorHandlerService

  ) {
    this.onBasicInfoChange = new EventEmitter<number>()
  }


  ngOnInit() {
  }

  openEditGroupModal() {
      let initialState = { daycareGroup: new DaycareGroup(this.daycareGroup.id, this.daycareGroup.groupName) }
      this.modalRef = this.bsModalService.show(DaycareGroupCreateEditComponent,
        { class: 'modal-top-10 modal-sm', initialState, ignoreBackdropClick: true })
      this.modalRef.content.onClose.subscribe(
        onClose => {
          if (onClose) {
            this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
              `Daycare group #${this.daycareGroup.id} has been succesfully edited.`)
            this.modalRef.content.onClose.subscribe(
              onClose => {
                this.onBasicInfoChange.emit(this.daycareGroup.id)
              })
          }
        })
  }

  deleteGroup() {
      this.modalRef = this.dialogModalService.openConfirmationModal(CONFIRMATION_HEADER,
        `You are about to delete daycare group #${this.daycareGroup.id} ${this.daycareGroup.groupName}.\n
      Any children, that are currently assigned to it will be left with no group.`)
      this.modalRef.content.onClose.subscribe(
        onClose => {
          if (onClose) {
            this.daycareGroupDataService.deleteDaycareGroup(this.daycareGroup.id).subscribe(
              response => {
                this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
                  `Daycare group #${this.daycareGroup.id} ${this.daycareGroup.groupName} has been succesfully deleted.`)
                this.modalRef.content.onClose.subscribe(
                  onClose => {
                    this.router.navigate(['daycare-group-list'])
                  })
              },
              err => {
                this.dialogModalService.openInformationModal(ERROR_HEADER, this.errorHandlerService.getErrorMessage(err))
              })
          }
        })
  }

}
