import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Moment } from 'moment';
import { DaycareGroup } from 'src/app/daycare-group/daycare-group';
import { JwtAuthService } from 'src/app/service/authentication/jwt-auth.service';
import { DaycareGroupDataService } from 'src/app/service/data/daycare-group-data.service';
import { DateHelperService } from 'src/app/service/date-helper.service';


@Component({
  selector: 'app-select-date-and-group',
  templateUrl: './select-date-and-group.component.html',
  styleUrls: ['./select-date-and-group.component.scss']
})

export class SelectDateAndGroupComponent implements OnInit {

  @Output('dataSelected') dataSelected = new EventEmitter<SelectedDateAndGroup>()

  minDate: Date
  selectedDate: Date
  daycareGroups: DaycareGroup[]
  selectedDaycareGroup: DaycareGroup

  weekDayFilter = (date: Date) => {
    return this.dateHelperService.isWeekDay(new Date(date))
  }

  constructor(
    public authService: JwtAuthService,
    private daycareGroupDataService: DaycareGroupDataService,
    private dateHelperService: DateHelperService
  ) { }

  ngOnInit(): void {
    this.minDate = new Date(2020, 0, 1)
    this.selectedDate = new Date()
    this.retrieveDaycareGroups()
  }

  retrieveDaycareGroups() {
    this.daycareGroupDataService.getAll().subscribe(
      daycareGroups => this.daycareGroups = daycareGroups
    )
  }

  onDateChange(moment: Moment) {
    this.selectedDate = moment.toDate()
  }

  onSelect() {
    const data: SelectedDateAndGroup = {
      date: this.selectedDate,
      group: this.selectedDaycareGroup
    }
    this.dataSelected.emit(data)
  }

}

export interface SelectedDateAndGroup {
  date: Date
  group: DaycareGroup
}
