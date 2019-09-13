import { Component, OnInit } from '@angular/core';
import { DaycareGroup } from '../daycare-group';
import { DaycareGroupDataService } from '../daycare-group-data.service';
import { ActivatedRoute, Router } from '@angular/router';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { DaycareGroupCreateEditComponent } from '../daycare-group-create-edit/daycare-group-create-edit.component';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';

@Component({
  selector: 'app-daycare-group-page',
  templateUrl: './daycare-group-page.component.html',
  styleUrls: ['./daycare-group-page.component.css']
})
export class DaycareGroupPageComponent implements OnInit {

  private bsModalRef: BsModalRef

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
    this.bsModalRef = this.bsModalService.show(DaycareGroupCreateEditComponent,
      { class: 'modal-top-10 modal-sm', initialState, ignoreBackdropClick: true })
    this.bsModalRef.content.onClose.subscribe(
      onClose => {
        if (onClose) {
          this.bsModalRef = this.dialogModalService.openInformationModal('Group edited',
            `Daycare group#${this.daycareGroup.id} has been succesfully edited.`)
          this.bsModalRef.content.onClose.subscribe(
            onClose => {
              this.retrieveDaycareGroup(this.daycareGroup.id)
            })
        }
      })
  }

  deleteGroup() {
    this.daycareGroupDataService.deleteDaycareGroup(this.daycareGroup.id).subscribe(
      response => {
        this.bsModalRef = this.dialogModalService.openInformationModal('Group deleted',
          `Daycare group#${this.daycareGroup.id} has been succesfully deleted.`)
        this.bsModalRef.content.onClose.subscribe(
          onClose => {
            this.router.navigate(['daycare-group-list'])
          })
      })
  }

  removeChildFromGroup(childId: number) {
    this.daycareGroupDataService.removeChildFromDaycareGroup(this.daycareGroup.id, childId).subscribe(
      response => {
        this.bsModalRef = this.dialogModalService.openInformationModal('Child removed',
          `Child#${childId} has been succesfully removed from daycare group#${this.daycareGroup.id}.`)
        this.bsModalRef.content.onClose.subscribe(
          onclose => {
            this.retrieveDaycareGroup(this.daycareGroup.id)
          })
      })
  }

}
