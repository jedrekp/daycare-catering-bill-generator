import { Component, EventEmitter, Input, OnChanges, Output, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { AssignGroupToSupervisorModalComponent } from '../assign-group-to-supervisor-modal/assign-group-to-supervisor-modal.component';
import { User } from '../user';

@Component({
  selector: 'app-group-supervisors-unassigned-profiles',
  templateUrl: './group-supervisors-unassigned-profiles.component.html',
  styleUrls: ['./group-supervisors-unassigned-profiles.component.scss']
})
export class GroupSupervisorsUnassignedProfilesComponent implements OnChanges {

  @Input('supervisors') supervisors: User[]
  @Input('filter') filter: (user: User, filter: string) => boolean
  @Output('groupAssigned') groupAssigned = new EventEmitter<boolean>()
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator


  tableDataSource: MatTableDataSource<User>
  displayedColumns: string[] = ['userId', 'firstName', 'lastName', 'username', 'assignGroup', 'profilePage']

  constructor(
    private dialog: MatDialog
  ) { }

  ngOnChanges(): void {
    this.setUpDataTable()
  }

  setUpDataTable() {
    this.tableDataSource = new MatTableDataSource(this.supervisors)
    this.tableDataSource.paginator = this.paginator
    this.tableDataSource.filterPredicate = this.filter
  }

  applyFilter(query: string) {
    this.tableDataSource.filter = query.trim().toLowerCase()

    if (this.tableDataSource.paginator)
      this.tableDataSource.paginator.firstPage()
  }

  openAssignGroupDialog(supervisorId: number) {
    const dialogRef = this.dialog.open(AssignGroupToSupervisorModalComponent, {
      disableClose: true,
      panelClass: 'dcbg-dialog',
      data: { supervisorId: supervisorId }
    })
    dialogRef.afterClosed().subscribe(
      supervisorId => {
        if (supervisorId)
          this.groupAssigned.emit(true)
      })
  }

}
