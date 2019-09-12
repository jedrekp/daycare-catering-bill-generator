import { Component, OnInit } from '@angular/core';
import { DaycareGroup } from '../daycare-group';
import { DaycareGroupDataService } from '../daycare-group-data.service';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { InformationModalComponent } from 'src/app/dialog/information-modal/information-modal.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-daycare-group-list',
  templateUrl: './daycare-group-list.component.html',
  styleUrls: ['./daycare-group-list.component.css']
})
export class DaycareGroupListComponent implements OnInit {
  private modalRef: BsModalRef
  private daycareGroups: DaycareGroup[] = []

  constructor(
    private router: Router,
    private modalService: BsModalService,
    private daycareGroupDataService: DaycareGroupDataService
  ) { }

  ngOnInit() {
    this.retrieveDaycareGroups()
  }

  retrieveDaycareGroups() {
    this.daycareGroupDataService.retrieveDaycareGroups().subscribe(
      daycareGroups => {
        this.daycareGroups = daycareGroups
      })
  }

  deleteGroup(groupId: number) {
    this.daycareGroupDataService.deleteDaycareGroup(groupId).subscribe(
      response => {
        this.openGroupDeletedInformationModal(groupId)
      })
  }

  openGroupDeletedInformationModal(groupId: number) {
    let initialState = { title: 'Group deleted', message: `Daycare group #${groupId} has been deleted.` }
    this.modalRef = this.modalService.show(InformationModalComponent,
      { class: 'modal-top-10 modal-md', initialState, ignoreBackdropClick: true })
    this.modalRef.content.onClose.subscribe(
      onClose => {
        this.retrieveDaycareGroups()
      })
  }

}
