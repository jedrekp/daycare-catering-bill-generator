import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DaycareGroupDataService } from 'src/app/service/data/daycare-group-data.service';
import { JwtAuthService } from 'src/app/service/authentication/jwt-auth.service';
import { UserDialogComponent } from 'src/app/user-dialog/user-dialog.component';
import { Child } from '../child';
import { ChildAssignToGroupModalComponent } from '../child-assign-to-group-modal/child-assign-to-group-modal.component';

@Component({
  selector: 'app-child-daycare-group-info',
  templateUrl: './child-daycare-group-info.component.html',
  styleUrls: ['./child-daycare-group-info.component.scss']
})
export class ChildDaycareGroupInfoComponent implements OnInit {

  @Input('child') child: Child
  @Output('daycareGroupChanged') childProfileChanged = new EventEmitter<number>()

  constructor(
    public authService: JwtAuthService,
    private daycareGroupDataService: DaycareGroupDataService,
    private dialog: MatDialog,
    private snackbar: MatSnackBar
  ) { }

  ngOnInit(): void {
  }

  openAssignToGroupDialog() {
    const dialogRef = this.dialog.open(ChildAssignToGroupModalComponent, {
      disableClose: true,
      panelClass: 'dcbg-dialog',
      data: { childId: this.child.id }
    })
    dialogRef.afterClosed().subscribe(
      childId => {
        if (childId)
          this.childProfileChanged.emit(childId)
      })
  }

  removeFromGroup() {
    const dialogRef = this.dialog.open(UserDialogComponent, {
      disableClose: true,
      panelClass: 'dcbg-dialog',
      data:
      {
        message:
          `You are about to remove this child from daycare group #${this.child.daycareGroup.id} (${this.child.daycareGroup.groupName}).`,
        cancelButton: true
      }
    })

    dialogRef.afterClosed().subscribe(
      onClose => {
        if (onClose)
          this.daycareGroupDataService.removeChildFromDaycareGroup(this.child.daycareGroup.id, this.child.id).subscribe(
            response => {
              this.snackbar.open(`Child #${this.child.id} is no longer assigned to group #${this.child.daycareGroup.id}.`)
              this.childProfileChanged.emit(this.child.id)
            })
      })
  }

}
