import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DaycareGroupDataService } from 'src/app/service/data/daycare-group-data.service';
import { DaycareGroup } from '../daycare-group';

@Component({
  selector: 'app-daycare-group-page',
  templateUrl: './daycare-group-page.component.html',
  styleUrls: ['./daycare-group-page.component.scss']
})
export class DaycareGroupPageComponent implements OnInit {

  daycareGroup: DaycareGroup

  constructor(
    private route: ActivatedRoute,
    private daycareGroupDataService: DaycareGroupDataService,
  ) { }

  ngOnInit() {
    this.retrieveDaycareGroup(this.route.snapshot.params['id'])
  }

  retrieveDaycareGroup(groupId: number) {
    this.daycareGroupDataService.getSingle(groupId).subscribe(
      daycarareGroup => {
        this.daycareGroup = daycarareGroup
      })
  }

}
