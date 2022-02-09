import { Component, Input, OnChanges, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { User } from '../user';

@Component({
  selector: 'app-group-supervisors-all-profiles',
  templateUrl: './group-supervisors-all-profiles.component.html',
  styleUrls: ['./group-supervisors-all-profiles.component.scss']
})
export class GroupSupervisorsAllProfilesComponent implements OnChanges {

  @Input('supervisors') supervisors: User[]
  @Input('filter') filter: (user: User, filter: string) => boolean
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator


  tableDataSource: MatTableDataSource<User>
  displayedColumns: string[] = ['userId', 'firstName', 'lastName', 'username', 'daycareGroup', 'profilePage']

  constructor() { }

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

}
