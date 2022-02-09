import { Component, EventEmitter, Input, OnChanges, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { Child } from 'src/app/child/child';
import { JwtAuthService } from 'src/app/service/authentication/jwt-auth.service';
import { DaycareGroupDataService } from 'src/app/service/data/daycare-group-data.service';
import { UserDialogComponent } from 'src/app/user-dialog/user-dialog.component';
import { DaycareGroup } from '../daycare-group';
import { DaycareGroupAssignChildModalComponent } from '../daycare-group-assign-child-modal/daycare-group-assign-child-modal.component';

@Component({
  selector: 'app-daycare-group-children-list',
  templateUrl: './daycare-group-children-list.component.html',
  styleUrls: ['./daycare-group-children-list.component.scss']
})
export class DaycareGroupChildrenListComponent implements OnChanges {

  @Input('daycareGroup') daycareGroup: DaycareGroup
  @Output('childRemovedOrAdded') childRemovedOrAdded = new EventEmitter<number>()

  tableDataSource: MatTableDataSource<Child>
  displayedColumns: string[] = ['childId', 'firstName', 'lastName', 'childPage', 'removeChild']

  constructor(
    public authService: JwtAuthService,
    private daycareGroupDataService: DaycareGroupDataService,
    private snackbar: MatSnackBar,
    private dialog: MatDialog
  ) { }

  ngOnChanges(): void {
    this.tableDataSource = new MatTableDataSource(this.daycareGroup.children)
  }

  removeChild(child: Child) {
    const dialogRef = this.dialog.open(UserDialogComponent, {
      disableClose: true,
      panelClass: 'dcbg-dialog',
      data:
      {
        message:
          `You are about to remove child #${child.id} (${child.firstName} ${child.lastName}) from this daycare group.`,
        cancelButton: true
      }
    })

    dialogRef.afterClosed().subscribe(
      onClose => {
        if (onClose)
          this.daycareGroupDataService.removeChildFromDaycareGroup(this.daycareGroup.id, child.id).subscribe(
            response => {
              this.snackbar.open(`Child #${child.id} (${child.firstName} ${child.lastName}) is no longer assigned to daycare group #${this.daycareGroup.id}.`)
              this.childRemovedOrAdded.emit(this.daycareGroup.id)
            })
      })
  }

  openAssignToGroupDialog() {
    const dialogRef = this.dialog.open(DaycareGroupAssignChildModalComponent, {
      disableClose: true,
      panelClass: 'dcbg-dialog',
      data: { daycareGroupId: this.daycareGroup.id }
    })

    dialogRef.afterClosed().subscribe(
      childId => {
        if (childId)
          this.childRemovedOrAdded.emit(this.daycareGroup.id)
      })
  }


}
