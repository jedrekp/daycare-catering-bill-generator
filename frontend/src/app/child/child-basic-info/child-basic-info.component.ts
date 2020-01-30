import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Child } from '../child';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { JwtAuthenticationService } from 'src/app/authentication/jwt-authentication.service';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { ChildDataService } from '../child-data.service';
import { ErrorHandlerService } from 'src/app/error/error-handler.service';
import { ChildCreateEditComponent } from '../child-create-edit/child-create-edit.component';
import { ACTION_COMPLETED_HEADER, ERROR_HEADER, CONFIRMATION_HEADER } from 'src/app/const';

@Component({
  selector: 'app-child-basic-info',
  templateUrl: './child-basic-info.component.html',
  styleUrls: ['./child-basic-info.component.css']
})
export class ChildBasicInfoComponent implements OnInit {

  @Input() private child: Child
  @Output() private onBasicInfoChange: EventEmitter<number>
  private modalRef: BsModalRef


  constructor(
    private authenticationService: JwtAuthenticationService,
    private bsModalService: BsModalService,
    private dialogModalService: DialogModalService,
    private childDataService: ChildDataService,
    private errorHandlerService: ErrorHandlerService
  ) {
    this.onBasicInfoChange = new EventEmitter<number>()
  }

  ngOnInit() { }

  openEditChildModal() {
      let initialState = {
        child: new Child(this.child.id, this.child.firstName, this.child.lastName,
          this.child.parentEmail, this.child.archived)
      }
      this.modalRef = this.bsModalService.show(ChildCreateEditComponent,
        { class: 'modal-top-10 modal-sm', initialState, ignoreBackdropClick: true })
      this.modalRef.content.onClose.subscribe(
        childId => {
          if (childId) {
            this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
              `Child #${this.child.id} has been succesfully edited.`)
            this.modalRef.content.onClose.subscribe(
              onClose => {
                this.onBasicInfoChange.emit(this.child.id)
              })
          }
        })
  }

  moveToArchive() {
      this.modalRef = this.dialogModalService.openConfirmationModal(CONFIRMATION_HEADER, `You are about to move child #${this.child.id} records to archive.\n
  Some actions might be unavailalbe while children records are in archive.\n
  This will also result in child being removed from daycare group that it's currently assigned to.`)
      this.modalRef.content.onClose.subscribe(
        onClose => {
          if (onClose) {
            this.childDataService.editChild(this.child.id,
              new Child(this.child.id, this.child.firstName, this.child.lastName, this.child.parentEmail, true)).subscribe(
                response => {
                  this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
                    `Child #${this.child.id} records have been moved to archive.`)
                  this.modalRef.content.onClose.subscribe(
                    onClose => {
                      this.onBasicInfoChange.emit(this.child.id)
                    })
                },
                err => {
                  this.modalRef = this.dialogModalService.openInformationModal(ERROR_HEADER, this.errorHandlerService.getErrorMessage(err))
                })
          }
        })
  }

  restoreFromArchive() {
      this.childDataService.editChild(this.child.id,
        new Child(this.child.id, this.child.firstName, this.child.lastName, this.child.parentEmail, false)).subscribe(
          response => {
            this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
              `Child #${this.child.id} records have been restored from archive.`)
            this.modalRef.content.onClose.subscribe(
              onclose => {
                this.onBasicInfoChange.emit(this.child.id)
              })
          },
          err => {
            this.modalRef = this.dialogModalService.openInformationModal(ERROR_HEADER, this.errorHandlerService.getErrorMessage(err))
          })
    } 

}

