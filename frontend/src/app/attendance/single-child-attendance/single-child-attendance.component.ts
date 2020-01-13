import { Component, OnInit, Input } from '@angular/core';
import { MonthlyChildAttendance } from '../monthly-child-attandance';
import { DatePipe } from '@angular/common';
import { ErrorHandlerService } from 'src/app/error/error-handler.service';
import { AttendanceDataService } from '../attendance-data.service';
import { ACTION_COMPLETED_HEADER, ERROR_HEADER } from 'src/app/const';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { BsModalRef } from 'ngx-bootstrap/modal/public_api';
import { Child } from 'src/app/child/child';

@Component({
  selector: 'app-single-child-attendance',
  templateUrl: './single-child-attendance.component.html',
  styleUrls: ['./single-child-attendance.component.css']
})
export class SingleChildAttendanceComponent implements OnInit {

  @Input() private child: Child
  private modalRef: BsModalRef
  private firstDayOfSelectedAttendanceMonth: Date
  private weekdaysFromSelectedMonth: Date[]
  private minDate: Date
  private monthlyChildAttendance: MonthlyChildAttendance
  private calendarSlicePoint: number

  constructor(
    private datePipe: DatePipe,
    private attendanceDataService: AttendanceDataService,
    private dialogModalService: DialogModalService,
    private errorHandlerService: ErrorHandlerService
  ) { }

  ngOnInit() {
    this.minDate = new Date(2019, 0, 1)
    let currentDate = new Date()
    this.firstDayOfSelectedAttendanceMonth = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1)
    this.retrieveAttendance()
  }

  onOpenCalendar(container) {
    container.monthSelectHandler = (event: any): void => {
      container._store.dispatch(container._actions.select(event.date))
    }
    container.setViewMode('month')
  }

  getAllWeekdaysFromSelectedMonth() {
    let date = new Date(this.firstDayOfSelectedAttendanceMonth)
    let month = date.getMonth()
    this.weekdaysFromSelectedMonth = []
    while (date.getMonth() === month) {
      if (!(date.getDay() == 0 || date.getDay() == 6)) {
        this.weekdaysFromSelectedMonth.push(new Date(date));
      }
      date.setDate(date.getDate() + 1);
    }
    this.calendarSlicePoint = Math.ceil(this.weekdaysFromSelectedMonth.length / 2)
  }

  retrieveAttendance() {
    this.getAllWeekdaysFromSelectedMonth()
    let monthString = this.datePipe.transform(this.firstDayOfSelectedAttendanceMonth, 'LLLL').toUpperCase()
    let year = this.firstDayOfSelectedAttendanceMonth.getFullYear()
    this.attendanceDataService.retrieveMonthlyAttendanceForChild(this.child.id, monthString, year).subscribe(
      monthlyChildAttendance => {
        this.monthlyChildAttendance = monthlyChildAttendance
      },
      err => {
        this.errorHandlerService.redirectToErrorPage(err)
      })
  }

  checkAttendanceStatus(date: Date) {
    let dateAsString = this.datePipe.transform(date, 'yyyy-MM-dd')
    if (this.monthlyChildAttendance.datesWhenPresent.indexOf(dateAsString) > -1) {
      return 1
    } else if (this.monthlyChildAttendance.datesWhenAbsent.indexOf(dateAsString) > -1) {
      return 0
    } else {
      return -1
    }
  }

  adjustDailyAttendance(date: Date, optionValue: number) {
    let dateAsString = this.datePipe.transform(date, 'yyyy-MM-dd')
    if (optionValue == 1) {
      let absentIndex = this.monthlyChildAttendance.datesWhenAbsent.indexOf(dateAsString)
      if (absentIndex > -1) {
        this.monthlyChildAttendance.datesWhenAbsent.splice(absentIndex, 1)
      }
      this.monthlyChildAttendance.datesWhenPresent.push(dateAsString)
    } else if (optionValue == 0) {
      let presentIndex = this.monthlyChildAttendance.datesWhenPresent.indexOf(dateAsString)
      if (presentIndex > -1) {
        this.monthlyChildAttendance.datesWhenPresent.splice(presentIndex, 1)
      }
      this.monthlyChildAttendance.datesWhenAbsent.push(dateAsString)
    }
  }

  submitAttendanceChanges() {
    let monthString = this.datePipe.transform(this.firstDayOfSelectedAttendanceMonth, 'LLLL').toUpperCase()
    let year = this.firstDayOfSelectedAttendanceMonth.getFullYear()
    this.attendanceDataService.submitMonthlyAttendanceForChild(this.child.id, monthString, year, this.monthlyChildAttendance).subscribe(
      response => {
        this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
          `${this.datePipe.transform(this.firstDayOfSelectedAttendanceMonth, 'MMMM yyyy')} attendance for child #${this.child.id} has been modified.`)
        this.modalRef.content.onClose.subscribe(
          onClose => {
            this.retrieveAttendance()
          })
      },
      err => {
        this.dialogModalService.openInformationModal(ERROR_HEADER, this.errorHandlerService.getErrorMessage(err))
      })
  }

}
