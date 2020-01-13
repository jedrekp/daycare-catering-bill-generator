import { Component, OnInit } from '@angular/core';
import { DaycareGroup } from '../daycare-group';
import { DaycareGroupDataService } from '../daycare-group-data.service';
import { ActivatedRoute } from '@angular/router';
import { ErrorHandlerService } from 'src/app/error/error-handler.service';

@Component({
  selector: 'app-daycare-group-page',
  templateUrl: './daycare-group-page.component.html',
  styleUrls: ['./daycare-group-page.component.css']
})
export class DaycareGroupPageComponent implements OnInit {

  private daycareGroup: DaycareGroup

  constructor(
    private route: ActivatedRoute,
    private daycareGroupDataService: DaycareGroupDataService,
    private errorHandlerService: ErrorHandlerService

  ) { }

  ngOnInit() {
    this.retrieveDaycareGroup(this.route.snapshot.params['groupId'])
  }

  retrieveDaycareGroup(groupId: number) {
    this.daycareGroupDataService.retrieveSingleDaycareGroup(groupId).subscribe(
      daycarareGroup => {
        this.daycareGroup = daycarareGroup
      },
      err => {
        this.errorHandlerService.redirectToErrorPage(err)
      })
  }

}
