import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { UserDataService } from 'src/app/service/data/user-data.service';
import { UserDialogComponent } from 'src/app/user-dialog/user-dialog.component';
import { GROUP_SUPERVISOR } from 'src/const';
import { AssignGroupToSupervisorModalComponent } from '../assign-group-to-supervisor-modal/assign-group-to-supervisor-modal.component';
import { User } from '../user';

@Component({
  selector: 'app-group-supervisor-page',
  templateUrl: './group-supervisor-page.component.html',
  styleUrls: ['./group-supervisor-page.component.scss']
})
export class GroupSupervisorPageComponent implements OnInit {

  supervisor: User

  constructor(
    private userDataService: UserDataService,
    private route: ActivatedRoute,
    private router: Router,
    private dialog: MatDialog,
    private snackbar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.retrieveGroupSupervisor(this.route.snapshot.params['username'])
  }

  retrieveGroupSupervisor(username: string) {
    this.userDataService.getSingleByUsername(username).subscribe(
      user => {
        if (user.daycareRole == GROUP_SUPERVISOR)
          this.supervisor = user
        else
          this.router.navigate(['error'], { state: { message: `User #${user.id} is not a group supervisor.` } });
      })
  }

  openAssignGroupDialog() {
    const dialogRef = this.dialog.open(AssignGroupToSupervisorModalComponent, {
      disableClose: true,
      panelClass: 'dcbg-dialog',
      data: { supervisorId: this.supervisor.id }
    })
    dialogRef.afterClosed().subscribe(
      supervisorId => {
        if (supervisorId)
          this.retrieveGroupSupervisor(this.supervisor.username)
      })
  }

  revokeGroupAssignment() {
    const dialogRef = this.dialog.open(UserDialogComponent, {
      disableClose: true,
      panelClass: 'dcbg-dialog',
      data:
      {
        message: `You are about to revoke group assigment from group supervisor 
        #${this.supervisor.id} (${this.supervisor.firstName} ${this.supervisor.lastName}).`
      }
    })
    dialogRef.afterClosed().subscribe(
      onClose => {
        if (onClose) {
          this.userDataService.removeDaycareGroupFromSupervisor(this.supervisor.id, this.supervisor.daycareGroup.id).subscribe(
            response => {
              this.snackbar.open(`Daycare group #${this.supervisor.daycareGroup.id} is no longer assigned to group supervisor #${this.supervisor.id} 
              (${this.supervisor.firstName} ${this.supervisor.lastName}).`)
              this.retrieveGroupSupervisor(this.supervisor.username)
            })
        }
      })
  }

  deleteGroupSupervisorAccount() {
    const dialogRef = this.dialog.open(UserDialogComponent, {
      disableClose: true,
      panelClass: 'dcbg-dialog',
      data:
      {
        message: `You are about to delete the account of group supervisor #${this.supervisor.id} (${this.supervisor.firstName} ${this.supervisor.lastName}).`
      }
    })

    dialogRef.afterClosed().subscribe(
      onClose => {
        if (onClose) {
          this.userDataService.deleteGroupSupervisorAccount(this.supervisor.id).subscribe(
            response => {
              this.snackbar.open(`Group supervisor #${this.supervisor.id} (${this.supervisor.firstName} ${this.supervisor.lastName}) account has been deleted.`)
              this.router.navigate(['group-supervisors'])
            })
        }
      })
  }

}

