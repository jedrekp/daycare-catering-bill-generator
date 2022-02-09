import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Child } from 'src/app/child/child';
import { DaycareGroupDataService } from 'src/app/service/data/daycare-group-data.service';

@Component({
  selector: 'app-daycare-group-assign-child-modal',
  templateUrl: './daycare-group-assign-child-modal.component.html',
  styleUrls: ['./daycare-group-assign-child-modal.component.scss']
})
export class DaycareGroupAssignChildModalComponent implements OnInit {

  unassignedChildren: Child[]
  selectedChild: Child

  constructor(
    public dialogRef: MatDialogRef<DaycareGroupAssignChildModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private daycareGroupDataService: DaycareGroupDataService,
    private snackbar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.retrieveChildren()
  }

  retrieveChildren() {
    this.daycareGroupDataService.getAllChildrenFromGroup(0).subscribe(
      children => this.unassignedChildren = children
    )
  }

  assignToGroup() {
    this.daycareGroupDataService.addChildToDaycareGroup(this.data.daycareGroupId, this.selectedChild.id).
      subscribe(
        daycareGroup => {
          this.dialogRef.close(this.selectedChild.id),
            this.snackbar.open(`Child #${this.selectedChild.id} is now assigned to group #${daycareGroup.id} (${daycareGroup.groupName}).`)
        },
        err => {
          this.dialogRef.close()
          throw err
        })
  }

}

