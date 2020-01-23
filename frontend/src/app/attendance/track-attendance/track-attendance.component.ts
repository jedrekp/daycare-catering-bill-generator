import { Component, OnInit } from '@angular/core';
import { DaycareGroupDataService } from 'src/app/daycare-group/daycare-group-data.service';
import { DaycareGroup } from 'src/app/daycare-group/daycare-group';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { AttendanceDataService } from '../attendance-data.service';
import { DailyGroupAttendance } from '../daily-group-attendance';
import { DatePipe } from '@angular/common';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { ACTION_COMPLETED_HEADER, ERROR_HEADER } from 'src/app/const';
import { Child } from 'src/app/child/child';
import { Router } from '@angular/router';
import { ErrorHandlerService } from 'src/app/error/error-handler.service';

@Component({
  selector: 'app-track-attendance',
  templateUrl: './track-attendance.component.html',
  styleUrls: ['./track-attendance.component.css']
})
export class TrackAttendanceComponent implements OnInit {

  private selectDateAndGroupForm: FormGroup
  private daycareGroups: DaycareGroup[]
  private selectedDaycareGroup: DaycareGroup
  private dailyAttendance: DailyGroupAttendance
  private minDate: Date

  constructor(
    private datePipe: DatePipe,
    private router: Router,
    private daycareGroupDataService: DaycareGroupDataService,
    private attendanceDataService: AttendanceDataService,
    private dialogModalService: DialogModalService,
    private errorHandlerService: ErrorHandlerService
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
      },
      err => {
        this.errorHandlerService.redirectToErrorPage(err)
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
                  for (let child of this.selectedDaycareGroup.children) {
                    this.markChildWithUntrackedAttendanceAsAbsentByDefault(child)
                  }
                },
                err => {
                  this.errorHandlerService.redirectToErrorPage(err)
                })
          },
          err => {
            this.errorHandlerService.redirectToErrorPage(err)
          })
    }
  }

  markChildWithUntrackedAttendanceAsAbsentByDefault(child: Child) {
    if (this.dailyAttendance.presentChildrenIds.indexOf(child.id) == -1 && this.dailyAttendance.absentChildrenIds.indexOf(child.id) == -1) {
      this.dailyAttendance.absentChildrenIds.push(child.id)
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
    this.attendanceDataService.submitDailyGroupAttendance(this.selectedDaycareGroup.id, this.dailyAttendance).subscribe(
      response => {
        this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
          `Attendance list for daycare group #${this.selectedDaycareGroup.id} (${this.selectedDaycareGroup.groupName})
           has been sucessfully submited.`)
      },
      err => {
        let errorMessage: string
        if (err.status == 401) {
          errorMessage = "Only headmaster and group supervisor assigned to this group can track attendance for it's members."
        } else {
          errorMessage = this.errorHandlerService.getErrorMessage(err)
        }
        this.dialogModalService.openInformationModal(ERROR_HEADER, errorMessage)
      })
  }


}

