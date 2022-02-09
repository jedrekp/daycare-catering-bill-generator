import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DaycareGroupDataService } from 'src/app/service/data/daycare-group-data.service';
import { DaycareGroup } from '../../daycare-group/daycare-group';

@Component({
  selector: 'app-child-assign-to-group-modal',
  templateUrl: './child-assign-to-group-modal.component.html',
  styleUrls: ['./child-assign-to-group-modal.component.scss']
})
export class ChildAssignToGroupModalComponent implements OnInit {

  daycareGroups: DaycareGroup[] = []
  selectedGroupId: number

  constructor(
    public dialogRef: MatDialogRef<ChildAssignToGroupModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private daycareGroupDataService: DaycareGroupDataService,
    private snackbar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.retrieveDaycareGroups()
  }

  retrieveDaycareGroups() {
    this.daycareGroupDataService.getAll().subscribe(
      groups => this.daycareGroups = groups,
      err => {
        this.dialogRef.close()
        throw err
      })
  }

  assignToGroup() {
    this.daycareGroupDataService.addChildToDaycareGroup(this.selectedGroupId, this.data.childId).
      subscribe(
        daycareGroup => {
          this.dialogRef.close(this.data.childId),
            this.snackbar.open(`Child #${this.data.childId} is now assigned to group #${daycareGroup.id} (${daycareGroup.groupName}).`)
        },
        err => {
          this.dialogRef.close()
          throw err
        })
  }

}