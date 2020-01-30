import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Child } from '../child';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { JwtAuthenticationService } from 'src/app/authentication/jwt-authentication.service';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { ErrorHandlerService } from 'src/app/error/error-handler.service';
import { AssignToGroupComponent } from 'src/app/daycare-group/assign-to-group/assign-to-group.component';
import { ACTION_COMPLETED_HEADER, ERROR_HEADER, CONFIRMATION_HEADER } from 'src/app/const';
import { DaycareGroupDataService } from 'src/app/daycare-group/daycare-group-data.service';

@Component({
  selector: 'app-child-daycare-group-info',
  templateUrl: './child-daycare-group-info.component.html',
  styleUrls: ['./child-daycare-group-info.component.css']
})
export class ChildDaycareGroupInfoComponent implements OnInit {

  @Input() private child: Child
  @Output() private onDaycareGroupChange: EventEmitter<number>
  private modalRef: BsModalRef


  constructor(
    private authenticationService: JwtAuthenticationService,
    private bsModalService: BsModalService,
    private dialogModalService: DialogModalService,
    private daycareGroupDataService: DaycareGroupDataService,
    private errorHandlerService: ErrorHandlerService
  ) {
    this.onDaycareGroupChange = new EventEmitter<number>()
  }

  ngOnInit() { }

  openAssignChildToGroupModal() {
      let initialState = { childId: this.child.id }
      this.modalRef = this.bsModalService.show(AssignToGroupComponent,
        { class: 'modal-top-10 modal-sm', initialState, ignoreBackdropClick: true })
      this.modalRef.content.onClose.subscribe(
        daycareGroup => {
          if (daycareGroup) {
            this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
              `Child #${this.child.id} is now assigned to daycare group #${daycareGroup.id}(${daycareGroup.groupName}).`)
            this.modalRef.content.onClose.subscribe(
              onClose => {
                this.onDaycareGroupChange.emit(this.child.id)
              })
          }
        })
  }

  removeFromGroup() {
      this.modalRef = this.dialogModalService.openConfirmationModal(CONFIRMATION_HEADER,
        `You are about to remove child #${this.child.id} from daycare group #${this.child.daycareGroup.id}(${this.child.daycareGroup.groupName}).`)
      this.modalRef.content.onClose.subscribe(
        onClose => {
          if (onClose) {
            this.daycareGroupDataService.removeChildFromDaycareGroup(this.child.daycareGroup.id, this.child.id).subscribe(
              response => {
                this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
                  `Child# ${this.child.id} is no longer assigned to daycare group #${this.child.daycareGroup.id}(${this.child.daycareGroup.groupName}).`)
                this.modalRef.content.onClose.subscribe(
                  onclose => {
                    this.onDaycareGroupChange.emit(this.child.id)
                  })
              },
              err => {
                this.dialogModalService.openInformationModal(ERROR_HEADER, this.errorHandlerService.getErrorMessage(err))
              })
          }
        })
    } 
  }
