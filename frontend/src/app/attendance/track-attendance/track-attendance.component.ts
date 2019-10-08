import { Component, OnInit, TemplateRef } from '@angular/core';
import { DaycareGroupDataService } from 'src/app/daycare-group/daycare-group-data.service';
import { DaycareGroup } from 'src/app/daycare-group/daycare-group';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { AttendanceDataService } from '../attendance-data.service';
import { DailyAttendance } from '../daily-attendance';
import { DatePipe } from '@angular/common';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';

@Component({
  selector: 'app-track-attendance',
  templateUrl: './track-attendance.component.html',
  styleUrls: ['./track-attendance.component.css']
})
export class TrackAttendanceComponent implements OnInit {

  private modalRef: BsModalRef
  private selectDateAndGroupForm: FormGroup
  private daycareGroups: DaycareGroup[] = []
  private selectedDaycareGroup: DaycareGroup
  private dailyAttendance: DailyAttendance
  private minDate: Date

  constructor(
    private bsModalService: BsModalService,
    private datePipe: DatePipe,
    private daycareGroupDataService: DaycareGroupDataService,
    private attendanceDataService: AttendanceDataService,
    private dialogModalService: DialogModalService
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

  markAsPresent(id: number) {
    let index = this.dailyAttendance.absentChildrenIds.indexOf(id)
    this.dailyAttendance.presentChildrenIds.push(
      this.dailyAttendance.absentChildrenIds.splice(index, 1)[0]
    )
  }

  markAsAbsent(id: number) {
    let index = this.dailyAttendance.presentChildrenIds.indexOf(id)
    this.dailyAttendance.absentChildrenIds.push(
      this.dailyAttendance.presentChildrenIds.splice(index, 1)[0])
  }

  submitAttendanceList() {
    this.attendanceDataService.submitAttendance(this.dailyAttendance).subscribe(
      response => {
        this.dialogModalService.openInformationModal('Action completed',
          `Attendance list for daycare group #${this.selectedDaycareGroup.id} (${this.selectedDaycareGroup.groupName})
           has been sucessfully submited.`)
      })
  }


}

