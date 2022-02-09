import { Component, Input, OnInit, Output, ViewEncapsulation, EventEmitter, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ChildDataService } from 'src/app/service/data/child-data.service';
import { JwtAuthService } from 'src/app/service/authentication/jwt-auth.service';
import { UserDialogComponent } from 'src/app/user-dialog/user-dialog.component';
import { Child } from '../child';
import { EditChildModalComponent } from '../edit-child-modal/edit-child-modal.component';

@Component({
  selector: 'app-child-basic-info',
  templateUrl: './child-basic-info.component.html',
  styleUrls: ['./child-basic-info.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ChildBasicInfoComponent implements OnInit {

  @Input('child') child: Child
  @Output('childProfileChanged') childProfileChanged = new EventEmitter<number>()

  constructor(
    public authService: JwtAuthService,
    private childDataService: ChildDataService,
    private dialog: MatDialog,
    private snackbar: MatSnackBar
  ) { }

  ngOnInit(): void {
  }

  openChildFormDialog() {
    const dialogRef = this.dialog.open(EditChildModalComponent, {
      disableClose: true,
      panelClass: 'dcbg-dialog',
      data:
      {
        child: this.child
      }
    })
    dialogRef.afterClosed().subscribe(
      childId => {
        if (childId)
          this.childProfileChanged.emit(childId)
      })

  }

  moveToArchive() {
    const dialogRef = this.dialog.open(UserDialogComponent, {
      disableClose: true,
      panelClass: 'dcbg-dialog',
      data:
      {
        message: `<p>You are about to move child #${this.child.id} records to archive.</p>
        <p>Most actions are unavailable for child profiles that are archived.</p>
        <p>Additionally, this action will remove the child from the daycare group that it's currently assigned to.</p>`
      }
    })

    dialogRef.afterClosed().subscribe(
      onClose => {
        if (onClose) {
          this.child.archived = true
          this.childDataService.update(this.child, this.child.id).subscribe(
            child => {
              this.childProfileChanged.emit(child.id)
              this.snackbar.open(`Child profile has been moved to archive.`)
            })
        }
      })
  }

  restoreFromArchive() {
    this.child.archived = false
    this.childDataService.update(this.child, this.child.id).subscribe(
      child => {
        this.childProfileChanged.emit(child.id)
        this.snackbar.open(`Child profile has been restored from archive.`)
      })
  }

}
