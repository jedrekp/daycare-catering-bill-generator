import { Component, OnInit } from '@angular/core';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { ActivatedRoute } from '@angular/router';
import { ChildDataService } from '../child-data.service';
import { Child, AssignedOption } from '../child';
import { ChildCreateEditComponent } from '../child-create-edit/child-create-edit.component';
import { ChildAssignOptionComponent } from '../child-assign-option/child-assign-option.component';
import { DaycareGroupDataService } from 'src/app/daycare-group/daycare-group-data.service';
import { AssignToGroupComponent } from 'src/app/daycare-group/assign-to-group/assign-to-group.component';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { CONFIRMATION_HEADER, ACTION_COMPLETED_HEADER, ERROR_HEADER } from 'src/app/const';

@Component({
  selector: 'app-child-page',
  templateUrl: './child-page.component.html',
  styleUrls: ['./child-page.component.css']
})
export class ChildPageComponent implements OnInit {

  private modalRef: BsModalRef

  private child: Child

  constructor(
    private route: ActivatedRoute,
    private bsModalService: BsModalService,
    private dialogModalService: DialogModalService,
    private childDataService: ChildDataService,
    private daycareGroupDataService: DaycareGroupDataService,
  ) { }

  ngOnInit() {
    this.child = new Child(-1, '', '', '',false)
    this.retrieveChild(this.route.snapshot.params['childId'])
  }

  retrieveChild(childId: number) {
    this.childDataService.retrieveChild(childId).subscribe(
      child => {
        this.child = child
        this.sortAssignedOptionsbyEffectiveDate(this.child.assignedOptions)
      })
  }

  sortAssignedOptionsbyEffectiveDate(assignedOptions: AssignedOption[]) {
    assignedOptions.sort((val1, val2) => {
      return <any>new Date(val2.effectiveDate) - <any>new Date(val1.effectiveDate)
    })
  }

  openEditChildModal() {
    let initialState = { child: new Child(this.child.id, this.child.firstName, this.child.lastName,
       this.child.parentEmail,this.child.archived) };
    this.modalRef = this.bsModalService.show(ChildCreateEditComponent,
      { class: 'modal-top-10 modal-sm', initialState, ignoreBackdropClick: true })
    this.modalRef.content.onClose.subscribe(
      childId => {
        if (childId) {
          this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
            `Child#${this.child.id} has been succesfully edited.`)
          this.modalRef.content.onClose.subscribe(
            onClose => {
              this.retrieveChild(this.child.id)
            })
        }
      })
  }

  openAssignChildToGroupModal() {
    let initialState = { childId: this.child.id }
    this.modalRef = this.bsModalService.show(AssignToGroupComponent,
      { class: 'modal-top-10 modal-sm', initialState, ignoreBackdropClick: true })
    this.modalRef.content.onClose.subscribe(
      daycareGroup => {
        if (daycareGroup) {
          this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
            `Child#${this.child.id} has been succesfully assigned to daycare group#${daycareGroup.id} (${daycareGroup.groupName}).`)
          this.modalRef.content.onClose.subscribe(
            onClose => {
              this.retrieveChild(this.child.id)
            })
        }
      })
  }

  removeFromGroup() {
    this.modalRef = this.dialogModalService.openConfirmationModal(CONFIRMATION_HEADER,
      `You are about to remove child#${this.child.id} from daycare group#${this.child.daycareGroup.id} (${this.child.daycareGroup.groupName}).`)
    this.modalRef.content.onClose.subscribe(
      onClose => {
        if (onClose) {
          this.daycareGroupDataService.removeChildFromDaycareGroup(this.child.daycareGroup.id, this.child.id).subscribe(
            response => {
              this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
                `Child#${this.child.id} is no longer assigned to daycare group#${this.child.daycareGroup.id} (${this.child.daycareGroup.groupName}).`)
              this.modalRef.content.onClose.subscribe(
                onclose => {
                  this.retrieveChild(this.child.id)
                })
            },
            err => {
              this.dialogModalService.openInformationModal(ERROR_HEADER, err.message)
            })
        }
      })
  }

  openAssignNewOptionModal() {
    let initialState = { childId: this.child.id }
    this.modalRef = this.bsModalService.show(ChildAssignOptionComponent,
      { class: 'modal-top-10 modal-sm', initialState, ignoreBackdropClick: true })
    this.modalRef.content.onClose.subscribe(
      cateringOption => {
        if (cateringOption) {
          this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
            `Catering option#${cateringOption.id} (${cateringOption.optionName}) has been succesfully assigned to child#${this.child.id}.`)
          this.modalRef.content.onClose.subscribe(
            onClose => {
              this.retrieveChild(this.child.id)
            })
        }
      })
  }

  removeAssignedOption(assignedOption: AssignedOption) {
    this.modalRef = this.dialogModalService.openConfirmationModal(CONFIRMATION_HEADER,
      `You are about to remove catering option#${assignedOption.cateringOption.id} (${assignedOption.cateringOption.optionName}) from child#${this.child.id}.\n
      This will have impact on future catering bills that cover months, for which this option is in effect.`)
    this.modalRef.content.onClose.subscribe(
      onClose => {
        if (onClose) {
          this.childDataService.removeAssignedOptionFromChild(this.child.id, assignedOption.id).subscribe(
            response => {
              this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
                `Catering option#${assignedOption.cateringOption.id} (${assignedOption.cateringOption.optionName}) is no longer assigned to child#${this.child.id}.`)
              this.retrieveChild(this.child.id);
            },
            err => {
              this.dialogModalService.openInformationModal(ERROR_HEADER, err.message)
            })
        }
      })
  }

}
