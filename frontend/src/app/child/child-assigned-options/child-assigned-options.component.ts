import { Component, EventEmitter, Input, OnChanges, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { ChildDataService } from 'src/app/service/data/child-data.service';
import { JwtAuthService } from 'src/app/service/authentication/jwt-auth.service';
import { UserDialogComponent } from 'src/app/user-dialog/user-dialog.component';
import { AssignedCateringOption, Child } from '../child';
import { ChildAssignOptionModalComponent } from '../child-assign-option-modal/child-assign-option-modal.component';

@Component({
  selector: 'app-child-assigned-options',
  templateUrl: './child-assigned-options.component.html',
  styleUrls: ['./child-assigned-options.component.scss']
})
export class ChildAssignedOptionsComponent implements OnChanges {

  @Input('child') child: Child
  @Output('assignedOptionsChanged') assignedOptionsChanged = new EventEmitter<number>()

  tableDataSource: MatTableDataSource<AssignedCateringOption>
  displayedColumns: string[] = ['effectiveFrom', 'optionName', 'action']

  constructor(
    public authService: JwtAuthService,
    private childDataService: ChildDataService,
    private dialog: MatDialog,
    private snackbar: MatSnackBar
  ) { }

  ngOnChanges(): void {
    this.tableDataSource = new MatTableDataSource(this.child.assignedOptions)
  }
  
  openAssignOptionDialog() {
    const dialogRef = this.dialog.open(ChildAssignOptionModalComponent, {
      disableClose: true,
      panelClass: 'dcbg-dialog',
      data: { childId: this.child.id }
    })
    dialogRef.afterClosed().subscribe(
      childId => {
        if (childId)
          this.assignedOptionsChanged.emit(childId)
      })
  }

  removeOption(assignedOption: AssignedCateringOption) {
    const dialogRef = this.dialog.open(UserDialogComponent, {
      disableClose: true,
      panelClass: 'dcbg-dialog',
      data:
      {
        message: `<p>You are about to remove a catering option assigned to this child.</p>
        <p>If you intend to change this catering option to a different one, add another option with new effective date instead.</p> 
        <p>You should only proceed if this catering option was mistakenly assigned and you need to correct an error.</p>`
      }
    })

    dialogRef.afterClosed().subscribe(
      onClose => {
        if (onClose)
          this.childDataService.removeAssignedOptionFromChild(this.child.id, assignedOption.effectiveDate).subscribe(
            () => {
              this.assignedOptionsChanged.emit(this.child.id)
              this.snackbar.open(`"${assignedOption.cateringOption.optionName}" 
              catering option (effective from: ${assignedOption.effectiveDate}) is no longer assigned to this child.`)
            })
      })
  }

}
