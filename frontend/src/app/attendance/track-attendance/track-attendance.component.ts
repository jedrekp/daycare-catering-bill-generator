import { Component, OnInit } from '@angular/core';
import { DaycareGroupDataService } from 'src/app/daycare-group/daycare-group-data.service';
import { DaycareGroup } from 'src/app/daycare-group/daycare-group';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { AttendanceDataService } from '../attendance-data.service';
import { DailyAttendance } from '../daily-attendance';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-track-attendance',
  templateUrl: './track-attendance.component.html',
  styleUrls: ['./track-attendance.component.css']
})
export class TrackAttendanceComponent implements OnInit {

  private selectDateAndGroupForm: FormGroup
  private daycareGroups: DaycareGroup[] = []
  private selectedDaycareGroup: DaycareGroup
  private dailyAttendance: DailyAttendance
  private minDate: Date

  constructor(
    private datePipe: DatePipe,
    private daycareGroupDataService: DaycareGroupDataService,
    private attendanceDataService: AttendanceDataService
  ) { }

  ngOnInit() {
    this.retrieveDaycareGroups();
    this.selectDateAndGroupForm = new FormGroup({
      daycareGroup: new FormControl(null, [Validators.required]),
      date: new FormControl(null, [Validators.required])
    })
    this.selectDateAndGroupForm.patchValue({ date: this.setCurrentDateOrFridayIfWeekend() })
    this.minDate = new Date(2019, 0, 1)
  }

  setCurrentDateOrFridayIfWeekend(): Date {
    let date = new Date();
    if (date.getDay() === 0) {
      date.setDate(date.getDate() + 5)
    } else if (date.getDay() === 6) {
      date.setDate(date.getDate() - 1)
    }
    return date
  }

  retrieveDaycareGroups() {
    this.daycareGroupDataService.retrieveDaycareGroups().subscribe(
      daycareGroups => {
        this.daycareGroups = daycareGroups;
        if (daycareGroups.length > 0) {
          this.selectDateAndGroupForm.patchValue({ daycareGroup: daycareGroups[0] })
        }
      })
  }

  onSelect() {
    if (this.selectDateAndGroupForm.valid) {
      this.daycareGroupDataService.retrieveSingleDaycareGroup(
        this.selectDateAndGroupForm.get('daycareGroup').value.id).subscribe(
          daycareGroup => {
            this.selectedDaycareGroup = daycareGroup
            this.attendanceDataService.retrieveDailyAttendanceForGroup(
              this.selectedDaycareGroup.id,
              this.datePipe.transform(this.selectDateAndGroupForm.get('date').value, 'yyyy-MM-dd')).subscribe(
                dailyAttendance => {
                  this.dailyAttendance = dailyAttendance
                })
          })
    }
  }

}
