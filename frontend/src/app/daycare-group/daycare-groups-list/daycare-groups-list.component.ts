import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { DaycareGroupDataService } from 'src/app/service/data/daycare-group-data.service';
import { JwtAuthService } from 'src/app/service/authentication/jwt-auth.service';
import { DaycareGroup } from '../daycare-group';

@Component({
  selector: 'app-daycare-groups-list',
  templateUrl: './daycare-groups-list.component.html',
  styleUrls: ['./daycare-groups-list.component.scss']
})
export class DaycareGroupsListComponent implements OnInit {

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator

  daycareGroups: DaycareGroup[];
  tableDataSource: MatTableDataSource<DaycareGroup>
  displayedColumns: string[] = ['groupId', 'groupName', 'groupSupervisor', 'groupPage']

  daycareGroupFilter = (group: DaycareGroup, filter: string) => {
    return group.groupName.toLowerCase().startsWith(filter)
  }

  constructor(
    public authService: JwtAuthService,
    private daycareGroupDataService: DaycareGroupDataService
  ) { }

  ngOnInit(): void {
    this.retrieveDaycareGroups()
  }

  retrieveDaycareGroups() {
    this.daycareGroupDataService.getAll().subscribe(
      daycareGroups => {
        this.daycareGroups = daycareGroups;
        this.setUpDataTable()
      })
  }

  setUpDataTable(){
    this.tableDataSource = new MatTableDataSource(this.daycareGroups)
    this.tableDataSource.paginator = this.paginator
    this.tableDataSource.filterPredicate = this.daycareGroupFilter
  }

  applyFilter(query: string) {
    this.tableDataSource.filter = query.trim().toLowerCase()

    if (this.tableDataSource.paginator)
      this.tableDataSource.paginator.firstPage()
  }

}
