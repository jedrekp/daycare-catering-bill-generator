import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { JwtAuthService } from 'src/app/service/authentication/jwt-auth.service';
import { DaycareGroupDataService } from 'src/app/service/data/daycare-group-data.service';
import { UserDialogComponent } from 'src/app/user-dialog/user-dialog.component';
import { DaycareGroup } from '../daycare-group';
import { EditDaycareGroupModalComponent } from '../edit-daycare-group-modal/edit-daycare-group-modal.component';

@Component({
  selector: 'app-daycare-group-basic-info',
  templateUrl: './daycare-group-basic-info.component.html',
  styleUrls: ['./daycare-group-basic-info.component.scss']
})
export class DaycareGroupBasicInfoComponent implements OnInit {

  @Input('daycareGroup') daycareGroup: DaycareGroup
  @Output('daycareGroupChanged') daycareGroupChanged = new EventEmitter<number>()

  constructor(
    public authService: JwtAuthService,
    private daycareGroupDataService: DaycareGroupDataService,
    private dialog: MatDialog,
    private snackbar: MatSnackBar,
    private router: Router
  ) { }

  ngOnInit(): void {
  }

  changeGroupName() {
    const dialogRef = this.dialog.open(EditDaycareGroupModalComponent, {
      disableClose: true,
      panelClass: 'dcbg-dialog',
      data:
      {
        daycareGroup: this.daycareGroup
      }
    })
    dialogRef.afterClosed().subscribe(
      groupId => {
        if (groupId)
          this.daycareGroupChanged.emit(groupId)
      })

  }
  

  deleteGroup() {
    const dialogRef = this.dialog.open(UserDialogComponent, {
      disableClose: true,
      panelClass: 'dcbg-dialog',
      data:
      {
        message: `You are about to delete daycare group #${this.daycareGroup.id} (${this.daycareGroup.groupName}).`,
        cancelButton: true
      }
    })
    dialogRef.afterClosed().subscribe(
      onClose => {
        if (onClose) {
          this.daycareGroupDataService.delete(this.daycareGroup.id).subscribe(
            response => {
              this.snackbar.open(`Daycare group #${this.daycareGroup.id} (${this.daycareGroup.groupName}) has been deleted.`)
              this.router.navigate(['daycare-groups'])
            })
        }
      })
  }

}
