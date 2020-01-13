import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';
import { Child, AssignedOption } from '../child';
import { JwtAuthenticationService } from 'src/app/authentication/jwt-authentication.service';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { ChildAssignOptionComponent } from '../child-assign-option/child-assign-option.component';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { ERROR_HEADER, ACTION_COMPLETED_HEADER, CONFIRMATION_HEADER } from 'src/app/const';
import { ChildDataService } from '../child-data.service';
import { ErrorHandlerService } from 'src/app/error/error-handler.service';

@Component({
  selector: 'app-child-assigned-options-list',
  templateUrl: './child-assigned-options-list.component.html',
  styleUrls: ['./child-assigned-options-list.component.css']
})
export class ChildAssignedOptionsListComponent implements OnInit {

  @Input() private child: Child
  @Output() private onAssignedOptionsChange: EventEmitter<number>
  private modalRef: BsModalRef


  constructor(
    private authenticationService: JwtAuthenticationService,
    private bsModalService: BsModalService,
    private dialogModalService: DialogModalService,
    private childDataService: ChildDataService,
    private errorHandlerService: ErrorHandlerService
  ) {
    this.onAssignedOptionsChange = new EventEmitter<number>()
  }

  ngOnInit() {}


  openAssignNewOptionModal() {
    if (this.authenticationService.getUserRole() == 'ROLE_HEADMASTER') {
      let initialState = { childId: this.child.id }
      this.modalRef = this.bsModalService.show(ChildAssignOptionComponent,
        { class: 'modal-top-10 modal-sm', initialState, ignoreBackdropClick: true })
      this.modalRef.content.onClose.subscribe(
        cateringOption => {
          if (cateringOption) {
            this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
              `Catering option #${cateringOption.id}(${cateringOption.optionName}) has been succesfully assigned to child #${this.child.id}.`)
            this.modalRef.content.onClose.subscribe(
              onClose => {
                this.onAssignedOptionsChange.emit(this.child.id)
              })
          }
        })
    } else {
      this.dialogModalService.openInformationModal(ERROR_HEADER, 'You are not authorized to perform this action.')
    }
  }

  removeAssignedOption(assignedOption: AssignedOption) {
    if (this.authenticationService.getUserRole() == 'ROLE_HEADMASTER') {
      this.modalRef = this.dialogModalService.openConfirmationModal(CONFIRMATION_HEADER,
        `You are about to remove catering option #${assignedOption.cateringOption.id}(${assignedOption.cateringOption.optionName}) from child #${this.child.id}.\n
      If you want to select a new catering option for this child, add another option with new effective date instead.\n.
      Proceed only if this option was mistakenly assigned and you need to correct an error.\n`)
      this.modalRef.content.onClose.subscribe(
        onClose => {
          if (onClose) {
            this.childDataService.removeAssignedOptionFromChild(this.child.id, assignedOption.effectiveDate).subscribe(
              response => {
                this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
                  `Catering option#${assignedOption.cateringOption.id} (${assignedOption.cateringOption.optionName}) is no longer assigned to child #${this.child.id}.`)
                this.onAssignedOptionsChange.emit(this.child.id)
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
