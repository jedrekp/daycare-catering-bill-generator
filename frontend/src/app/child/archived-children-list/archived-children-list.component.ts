import { Component, OnInit } from '@angular/core';
import { ChildDataService } from '../child-data.service';
import { Child } from '../child';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { BsModalRef } from 'ngx-bootstrap/modal/public_api';
import { ACTION_COMPLETED_HEADER, ERROR_HEADER } from 'src/app/const';

@Component({
  selector: 'app-archived-children-list',
  templateUrl: './archived-children-list.component.html',
  styleUrls: ['./archived-children-list.component.css']
})
export class ArchivedChildrenListComponent implements OnInit {

  private modalRef: BsModalRef
  private archivedChildren: Child[] = []

  constructor(
    private dialogModalService: DialogModalService,
    private childDataService: ChildDataService
  ) { }

  ngOnInit() {
    this.retrieveArchivedChildren()
  }

  retrieveArchivedChildren() {
    this.childDataService.retrieveArchivedChildren().subscribe(
      children => this.archivedChildren = children
    )
  }

  restoreFromArchive(child: Child) {
    this.childDataService.editChild(child.id,
      new Child(child.id, child.firstName, child.lastName, child.parentEmail, false)).subscribe(
        child => {
          this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
            `Child #${child.id} (${child.firstName} ${child.lastName}) has been restored from archive.`)
          this.modalRef.content.onClose.subscribe(
            onClose => {
              this.retrieveArchivedChildren()
            })
        },
        err => {
          this.modalRef = this.dialogModalService.openInformationModal(ERROR_HEADER, err.message)
        })
  }

}
