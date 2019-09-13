import { Component, OnInit } from '@angular/core';
import { DaycareGroup } from '../daycare-group';
import { DaycareGroupDataService } from '../daycare-group-data.service';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { InformationModalComponent } from 'src/app/dialog/information-modal/information-modal.component';
import { Router } from '@angular/router';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';

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
    private dialogModalService: DialogModalService,
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
        this.modalRef = this.dialogModalService.openInformationModal('Group deleted',
          `Daycare group#${groupId} has been succesfully deleted.`)
        this.modalRef.content.onClose.subscribe(
          onclose => {
            this.retrieveDaycareGroups()
          })
      })
  }

}
