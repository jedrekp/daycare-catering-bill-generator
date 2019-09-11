import { Component, OnInit } from '@angular/core';
import { DaycareGroup } from '../daycare-group';
import { DaycareGroupDataService } from '../daycare-group-data.service';
import { ActivatedRoute, Router } from '@angular/router';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { DaycareGroupCreateEditComponent } from '../daycare-group-create-edit/daycare-group-create-edit.component';

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
    private modalService: BsModalService,
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
    this.modalRef = this.modalService.show(DaycareGroupCreateEditComponent,
      { class: 'modal-top-10 modal-sm', initialState, ignoreBackdropClick: true })
    this.modalRef.content.onClose.subscribe(
      onClose => {
        if (onClose) {
          this.retrieveDaycareGroup(this.daycareGroup.id)
        }
      })
  }

  deleteGroup() {
    this.daycareGroupDataService.deleteDaycareGroup(this.daycareGroup.id).subscribe(
      response => {
        this.router.navigate([''])
      })
  }

  removeChildFromGroup(childId: number) {
    this.daycareGroupDataService.removeChildFromDaycareGroup(this.daycareGroup.id, childId).subscribe(
      response => {
        this.retrieveDaycareGroup(this.daycareGroup.id)
      })
  }
}
