import { Component, Input, OnChanges, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Child } from '../child';

@Component({
  selector: 'app-child-active-profiles',
  templateUrl: './child-active-profiles.component.html',
  styleUrls: ['./child-active-profiles.component.scss']
})
export class ChildActiveProfilesComponent implements OnChanges {

  @Input('children') children: Child[]
  @Input('filter') filter: (child: Child, filter: string) => boolean
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator


  tableDataSource: MatTableDataSource<Child>
  displayedColumns: string[] = ['childId', 'firstName', 'lastName', 'daycareGroup', 'childPage']

  constructor() { }

  ngOnChanges(): void {
    this.setUpDataTable()
  }

  setUpDataTable() {
    this.tableDataSource = new MatTableDataSource(this.children)
    this.tableDataSource.paginator = this.paginator
    this.tableDataSource.filterPredicate = this.filter
  }

  applyFilter(query: string) {
    this.tableDataSource.filter = query.trim().toLowerCase()

    if (this.tableDataSource.paginator)
      this.tableDataSource.paginator.firstPage()
  }

}
