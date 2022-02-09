import { Component, Input, OnChanges, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Child } from '../child';

@Component({
  selector: 'app-child-archived-profiles',
  templateUrl: './child-archived-profiles.component.html',
  styleUrls: ['./child-archived-profiles.component.scss']
})
export class ChildArchivedProfilesComponent implements OnChanges {

  @Input('children') children: Child[]
  @Input('filter') filter: (child: Child, filter: string) => boolean
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator


  tableDataSource: MatTableDataSource<Child>
  displayedColumns: string[] = ['childId', 'firstName', 'lastName', 'childPage']

  constructor() { }

  ngOnChanges(): void {
    this.setUpDataTable()
  }

  setUpDataTable() {
    this.tableDataSource = new MatTableDataSource(this.children)
    this.paginator._intl.itemsPerPageLabel = 'Profiles/Page';
    this.tableDataSource.paginator = this.paginator
    this.tableDataSource.filterPredicate = this.filter
  }

  applyFilter(query: string) {
    this.tableDataSource.filter = query.trim().toLowerCase()

    if (this.tableDataSource.paginator)
      this.tableDataSource.paginator.firstPage()
  }

}