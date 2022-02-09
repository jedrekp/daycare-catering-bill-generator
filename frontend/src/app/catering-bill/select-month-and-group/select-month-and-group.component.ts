import { EventEmitter } from '@angular/core';
import { Component, OnInit, Output } from '@angular/core';
import { MAT_DATE_FORMATS } from '@angular/material/core';
import { MatDatepicker } from '@angular/material/datepicker';
import { Moment } from 'moment';
import { DaycareGroup } from 'src/app/daycare-group/daycare-group';
import { DaycareGroupDataService } from 'src/app/service/data/daycare-group-data.service';
import { DateHelperService } from 'src/app/service/date-helper.service';
import { MONTH_PICKER_FORMAT } from 'src/const';

@Component({
  selector: 'app-select-month-and-group',
  templateUrl: './select-month-and-group.component.html',
  styleUrls: ['./select-month-and-group.component.scss'],
  providers: [{ provide: MAT_DATE_FORMATS, useValue: MONTH_PICKER_FORMAT }]
})
export class SelectMonthAndGroupComponent implements OnInit {

  @Output('dataSelected') dateSelected = new EventEmitter<SelectedMonthAndGroup>()

  minDate: Date
  firstDayOfMonth: Date
  daycareGroups: DaycareGroup[]
  selectedDaycareGroup: DaycareGroup

  constructor(
    private daycareGroupDataService: DaycareGroupDataService,
    private dateHelperService: DateHelperService
  ) { }

  ngOnInit(): void {
    this.minDate = new Date(2020, 0, 1)
    const currentDate = new Date()
    this.firstDayOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1)
    this.retrieveDaycareGroups()
  }

  retrieveDaycareGroups() {
    this.daycareGroupDataService.getAll().subscribe(
      groups => this.daycareGroups = groups
    )
  }

  onMonthSelected(moment: Moment, datepicker: MatDatepicker<Moment>) {
    datepicker.close()
    this.firstDayOfMonth = moment.toDate()
  }

  onSelect() {
    const data = {
      month: this.dateHelperService.getMonthAsString(this.firstDayOfMonth),
      year: this.firstDayOfMonth.getFullYear(),
      group: this.selectedDaycareGroup
    }
    this.dateSelected.emit(data)
  }

}

export interface SelectedMonthAndGroup {
  month: string,
  year: number
  group: DaycareGroup
}
