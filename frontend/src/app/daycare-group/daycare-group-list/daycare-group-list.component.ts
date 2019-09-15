import { Component, OnInit } from '@angular/core';
import { DaycareGroup } from '../daycare-group';
import { DaycareGroupDataService } from '../daycare-group-data.service';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Router } from '@angular/router';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { CONFIRMATION_HEADER, ERROR_HEADER } from 'src/app/const';

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

  deleteGroup(daycareGroup: DaycareGroup) {
    this.modalRef = this.dialogModalService.openConfirmationModal(CONFIRMATION_HEADER,
      `You are about to delete daycare group#${daycareGroup.id}.\n
      Any children, that are currently assigned to it, will be left with no group.`)
    this.modalRef.content.onClose.subscribe(
      onClose => {
        if (onClose) {
          this.daycareGroupDataService.deleteDaycareGroup(daycareGroup.id).subscribe(
            response => {
              this.modalRef = this.dialogModalService.openInformationModal('Group deleted',
                `Daycare group#${daycareGroup.id} (${daycareGroup.groupName}) has been succesfully deleted.`)
              this.modalRef.content.onClose.subscribe(
                onclose => {
                  this.retrieveDaycareGroups()
                })
            },
            err => {
              this.dialogModalService.openInformationModal(ERROR_HEADER,err.message)
            })
        }
      })
  }

}
