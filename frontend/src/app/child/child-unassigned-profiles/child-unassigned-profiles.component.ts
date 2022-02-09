import { Component, EventEmitter, Input, OnChanges, Output, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { JwtAuthService } from 'src/app/service/authentication/jwt-auth.service';
import { Child } from '../child';
import { ChildAssignToGroupModalComponent } from '../child-assign-to-group-modal/child-assign-to-group-modal.component';

@Component({
  selector: 'app-child-unassigned-profiles',
  templateUrl: './child-unassigned-profiles.component.html',
  styleUrls: ['./child-unassigned-profiles.component.scss']
})
export class ChildUnassignedProfilesComponent implements OnChanges {

  @Input('children') children: Child[]
  @Input('filter') filter: (child: Child, filter: string) => boolean
  @Output('groupAssigned') groupAssigned = new EventEmitter<boolean>()
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator

  tableDataSource: MatTableDataSource<Child>
  displayedColumns: string[] = ['childId', 'firstName', 'lastName', 'assignToGroup', 'childPage']

  constructor(
    public authService: JwtAuthService,
    private dialog: MatDialog
  ) { }

  ngOnChanges(): void {
    this.setUpDataTable()
  }

  setUpDataTable() {
    this.tableDataSource = new MatTableDataSource(this.children)
    this.paginator._intl.itemsPerPageLabel = 'Profiles/Page:';
    this.tableDataSource.paginator = this.paginator
    this.tableDataSource.filterPredicate = this.filter
  }

  applyFilter(query: string) {
    this.tableDataSource.filter = query.trim().toLowerCase()

    if (this.tableDataSource.paginator)
      this.tableDataSource.paginator.firstPage()
  }

  openAssignToGroupDialog(child: Child) {
    const dialogRef = this.dialog.open(ChildAssignToGroupModalComponent, {
      disableClose: true,
      panelClass: 'dcbg-dialog',
      data: { childId: child.id }
    })
    dialogRef.afterClosed().subscribe(
      childId => {
        if (childId)
          this.groupAssigned.emit(true)
      })
  }

}
