import { Component, OnInit } from '@angular/core';
import { DaycareGroup } from '../daycare-group';
import { DaycareGroupDataService } from '../daycare-group-data.service';
import { ActivatedRoute, Router } from '@angular/router';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { DaycareGroupCreateEditComponent } from '../daycare-group-create-edit/daycare-group-create-edit.component';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { Child } from 'src/app/child/child';
import { CONFIRMATION_HEADER, ERROR_HEADER } from 'src/app/const';

@Component({
  selector: 'app-daycare-group-page',
  templateUrl: './daycare-group-page.component.html',
  styleUrls: ['./daycare-group-page.component.css']
})
export class DaycareGroupPageComponent implements OnInit {

  private modalRef: BsModalRef

  private daycareGroup: DaycareGroup

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private bsModalService: BsModalService,
    private dialogModalService: DialogModalService,
    private daycareGroupDataService: DaycareGroupDataService

  ) { }

  ngOnInit() {
    this.daycareGroup = new DaycareGroup(-1, '')
    this.retrieveDaycareGroup(this.route.snapshot.params['groupId'])
  }

  retrieveDaycareGroup(groupId: number) {
    this.daycareGroupDataService.retrieveSingleDaycareGroup(groupId).subscribe(
      daycarareGroup => {
        this.daycareGroup = daycarareGroup
        this.sortChildrenByLastName()
      })
  }

  sortChildrenByLastName() {
    this.daycareGroup.children.sort(function (a, b) {
      if (a.lastName < b.lastName) { return -1; }
      if (a.lastName > b.lastName) { return 1; }
      return 0;
    })
  }

  openEditGroupModal() {
    let initialState = { daycareGroup: new DaycareGroup(this.daycareGroup.id, this.daycareGroup.groupName) }
    this.modalRef = this.bsModalService.show(DaycareGroupCreateEditComponent,
      { class: 'modal-top-10 modal-sm', initialState, ignoreBackdropClick: true })
    this.modalRef.content.onClose.subscribe(
      onClose => {
        if (onClose) {
          this.modalRef = this.dialogModalService.openInformationModal('Group edited',
            `Daycare group#${this.daycareGroup.id} has been succesfully edited.`)
          this.modalRef.content.onClose.subscribe(
            onClose => {
              this.retrieveDaycareGroup(this.daycareGroup.id)
            })
        }
      })
  }

  deleteGroup() {
    this.modalRef = this.dialogModalService.openConfirmationModal(CONFIRMATION_HEADER,
      `You are about to delete daycare group#${this.daycareGroup.id}.\n
      Any children, that are currently assigned to it will be left with no group.`)
    this.modalRef.content.onClose.subscribe(
      onClose => {
        if (onClose) {
          this.daycareGroupDataService.deleteDaycareGroup(this.daycareGroup.id).subscribe(
            response => {
              this.modalRef = this.dialogModalService.openInformationModal('Group deleted',
                `Daycare group#${this.daycareGroup.id} has been succesfully deleted.`)
              this.modalRef.content.onClose.subscribe(
                onClose => {
                  this.router.navigate(['daycare-group-list'])
                })
            },
            err => {
              this.dialogModalService.openInformationModal(ERROR_HEADER, err.message)
            })
        }
      })
  }

  removeChildFromGroup(child: Child) {
    this.modalRef = this.dialogModalService.openConfirmationModal(CONFIRMATION_HEADER,
      `You are about to remove child#${child.id} (${child.firstName} ${child.lastName}) from daycare group#${this.daycareGroup.id}`)
    this.modalRef.content.onClose.subscribe(
      onclose => {
        if (onclose) {
          this.daycareGroupDataService.removeChildFromDaycareGroup(this.daycareGroup.id, child.id).subscribe(
            response => {
              this.modalRef = this.dialogModalService.openInformationModal('Child removed',
                `Child#${child.id} (${child.firstName} ${child.lastName}) has been succesfully removed from daycare group#${this.daycareGroup.id}.`)
              this.modalRef.content.onClose.subscribe(
                onclose => {
                  this.retrieveDaycareGroup(this.daycareGroup.id)
                })
            },
            err => {
              this.dialogModalService.openInformationModal(ERROR_HEADER, err.message)
            })
        }
      })
  }

}
