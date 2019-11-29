import { Component, OnInit } from '@angular/core';
import { Child } from '../child';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { AssignToGroupComponent } from 'src/app/daycare-group/assign-to-group/assign-to-group.component';
import { ACTION_COMPLETED_HEADER } from 'src/app/const';
import { DaycareGroupDataService } from 'src/app/daycare-group/daycare-group-data.service';

@Component({
  selector: 'app-unassigned-children-list',
  templateUrl: './unassigned-children-list.component.html',
  styleUrls: ['./unassigned-children-list.component.css']
})
export class UnassignedChildrenListComponent implements OnInit {

  private modalRef: BsModalRef
  private children: Child[] = []

  constructor(
    private dialogModalService: DialogModalService,
    private bsModalService: BsModalService,
    private daycareGroupDataService: DaycareGroupDataService
  ) { }

  ngOnInit() {
    this.retrieveChildren()
  }

  retrieveChildren() {
    this.daycareGroupDataService.getChildrenFromGroup(0).subscribe(
      children => {
        this.children = children
      })
  }

  openAssignChildToGroupModal(child: Child) {
    let initialState = { childId: child.id }
    this.modalRef = this.bsModalService.show(AssignToGroupComponent,
      { class: 'modal-top-10 modal-sm', initialState, ignoreBackdropClick: true })
    this.modalRef.content.onClose.subscribe(
      daycareGroup => {
        if (daycareGroup) {
          this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
            `Child #${child.id} (${child.firstName} ${child.lastName})` +
            ` has been succesfully assigned to daycare group #${daycareGroup.id} (${daycareGroup.groupName}).`)
          this.modalRef.content.onClose.subscribe(
            onClose => {
              this.retrieveChildren()
            })
        }
      })
  }

}
