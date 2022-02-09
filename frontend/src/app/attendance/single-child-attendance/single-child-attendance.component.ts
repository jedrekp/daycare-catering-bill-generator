import { Component, Input, OnChanges, OnInit } from '@angular/core';
import { MAT_DATE_FORMATS } from '@angular/material/core';
import { MatDatepicker } from '@angular/material/datepicker';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { Moment } from 'moment';
import { DateHelperService } from 'src/app/service/date-helper.service';
import { AttendanceDataService } from 'src/app/service/data/attendance-data.service';
import { JwtAuthService } from 'src/app/service/authentication/jwt-auth.service';
import { MONTH_PICKER_FORMAT } from 'src/const';
import { Child } from '../../child/child';
import { MonthlyChildAttendance } from 'src/app/attendance/monthly-child-attendance';

@Component({
  selector: 'app-single-child-attendance',
  templateUrl: './single-child-attendance.component.html',
  styleUrls: ['./single-child-attendance.component.scss'],
  providers: [{ provide: MAT_DATE_FORMATS, useValue: MONTH_PICKER_FORMAT }]
})

export class SingleChildAttendanceComponent implements OnInit, OnChanges {

  @Input('child') child: Child

  attendance: MonthlyChildAttendance
  minDate: Date
  firstDayOfMonth: Date
  weekdaysFromMonth: Date[]
  tableDataSource: MatTableDataSource<Date>
  displayedColumns: string[] = ['date', 'present', 'absent']

  constructor(
    public authService: JwtAuthService,
    private attendanceDataService: AttendanceDataService,
    private dateHelperService: DateHelperService,
    private snackbar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.minDate = new Date(2020, 0, 1)
    const currentDate = new Date()
    this.firstDayOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1)
    this.setUpDataTable()
    this.retrieveAttendance()
  }

  ngOnChanges() {
    if (this.attendance) {
      if (this.child.id != this.attendance.childId)
        this.ngOnInit()
      else if (this.child.archived)
        this.retrieveAttendance()
    }
  }

  setUpDataTable() {
    this.attendance = null
    this.weekdaysFromMonth = this.dateHelperService.getAllWeekdaysFromMonth(this.firstDayOfMonth)
    this.tableDataSource = new MatTableDataSource(this.weekdaysFromMonth)
  }

  retrieveAttendance() {
    const month = this.dateHelperService.getMonthAsString(this.firstDayOfMonth)
    const year = this.firstDayOfMonth.getFullYear()
    this.attendanceDataService.getMonthlyAttendanceForChild(this.child.id, month.toUpperCase(), year).subscribe(
      data => this.attendance = data
    )
  }

  onMonthSelected(moment: Moment, datepicker: MatDatepicker<Moment>) {
    datepicker.close()
    this.firstDayOfMonth = moment.toDate()
    this.setUpDataTable()
    this.retrieveAttendance()
  }

  submitAttendanceChanges() {
    const month = this.dateHelperService.getMonthAsString(this.firstDayOfMonth)
    const year = this.firstDayOfMonth.getFullYear()
    this.attendanceDataService.submitMonthlyAttendanceForChild(this.attendance, month.toUpperCase(), year).subscribe(
      response => this.snackbar.open('Attendance changes successfully submited.')
    )
  }

  isPresent(date: Date) {
    const stringDate = this.dateHelperService.getDateAsString(date)
    return this.attendance.datesWhenPresent.indexOf(stringDate) > -1
  }

  isAbsent(date: Date) {
    const stringDate = this.dateHelperService.getDateAsString(date)
    return this.attendance.datesWhenAbsent.indexOf(stringDate) > -1
  }

  setAttendanceForAllDates(isPresent: boolean) {
    this.weekdaysFromMonth.forEach(date => this.setAttendanceStatus(date, isPresent))
  }

  setAttendanceStatus(date: Date, isPresent: boolean) {
    const stringDate = this.dateHelperService.getDateAsString(date)
    const presentIndex = this.attendance.datesWhenPresent.indexOf(stringDate)
    const absentIndex = this.attendance.datesWhenAbsent.indexOf(stringDate)
    if (isPresent)
      this.setToPresent(stringDate, presentIndex, absentIndex)
    else
      this.setToAbsent(stringDate, presentIndex, absentIndex)
  }

  private setToPresent(date: string, presentIndex: number, absentIndex: number) {
    if (presentIndex == -1)
      this.attendance.datesWhenPresent.push(date)
    if (absentIndex > -1)
      this.attendance.datesWhenAbsent.splice(absentIndex, 1)
  }

  private setToAbsent(date: string, presentIndex: number, absentIndex: number) {
    if (presentIndex > -1)
      this.attendance.datesWhenPresent.splice(presentIndex, 1)
    if (absentIndex == -1)
      this.attendance.datesWhenAbsent.push(date)
  }

}


