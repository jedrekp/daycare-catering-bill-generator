import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DaycareGroup } from 'src/app/daycare-group/daycare-group';
import { DaycareGroupDataService } from 'src/app/service/data/daycare-group-data.service';
import { UserDataService } from 'src/app/service/data/user-data.service';

@Component({
  selector: 'app-assign-group-to-supervisor-modal',
  templateUrl: './assign-group-to-supervisor-modal.component.html',
  styleUrls: ['./assign-group-to-supervisor-modal.component.scss']
})
export class AssignGroupToSupervisorModalComponent implements OnInit {

  daycareGroups: DaycareGroup[] = []
  selectedGroupId: number

  constructor(
    public dialogRef: MatDialogRef<AssignGroupToSupervisorModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private daycageGroupDataService: DaycareGroupDataService,
    private userDataService: UserDataService,
    private snackbar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.retrieveUnassignedDaycareGroups()
  }

  retrieveUnassignedDaycareGroups() {
    this.daycageGroupDataService.getAllByGroupSupervisorId(0).subscribe(
      groups => this.daycareGroups = groups,
      err => {
        this.dialogRef.close()
        throw err
      })
  }

  assignGroup() {
    this.userDataService.assignDaycareGroupToSupervisor(this.data.supervisorId, this.selectedGroupId).subscribe(
      user => {
        this.dialogRef.close(this.data.supervisorId),
          this.snackbar.open(`Daycare group #${user.daycareGroup.id} (${user.daycareGroup.groupName}) is now assigned to ${user.firstName} ${user.lastName}.`)
      },
      err => {
        this.dialogRef.close()
        throw err
      })
  }

}