import { Component, OnInit } from '@angular/core';
import { DaycareGroup } from '../daycare-group';
import { DaycareGroupDataService } from '../daycare-group-data.service';

@Component({
  selector: 'app-daycare-group-list',
  templateUrl: './daycare-group-list.component.html',
  styleUrls: ['./daycare-group-list.component.css']
})
export class DaycareGroupListComponent implements OnInit {
  private daycareGroups: DaycareGroup[] = []

  constructor(
    private daycareGroupDataService: DaycareGroupDataService
  ) { }

  ngOnInit() {
    this.retrieveDaycareGroups()
  }

  retrieveDaycareGroups() {
    this.daycareGroupDataService.retrieveDaycareGroups().subscribe(
      daycareGroups => {
        this.daycareGroups = daycareGroups
      })
  }

  deleteGroup(groupId: number) {
    this.daycareGroupDataService.deleteDaycareGroup(groupId).subscribe(
      response => {
        this.retrieveDaycareGroups()
      })
  }

}
