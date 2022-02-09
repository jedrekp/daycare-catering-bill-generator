import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError } from 'rxjs/operators';
import { DailyGroupAttendance } from 'src/app/attendance/daily-group-attendance';
import { MonthlyChildAttendance } from 'src/app/attendance/monthly-child-attendance';
import { API_URL } from 'src/const';
import { CrudDataService } from './crud-data.service';

@Injectable({
  providedIn: 'root'
})
export class AttendanceDataService extends CrudDataService {

  constructor(http: HttpClient) {
    super(`${API_URL}/attendanceSheets`, http)
  }

  getMonthlyAttendanceForChild(childId: number, month: string, year: number) {
    const params = new HttpParams()
      .set('childId', childId.toString())
      .set('month', month)
      .set('year', year.toString())
    return this.http.get<MonthlyChildAttendance>(this.resourceUrl, { params: params })
      .pipe(
        catchError(this.handleError)
      )
  }

  getDailyAttendanceForGroup(daycareGroupId: number, date: string) {
    const params = new HttpParams()
      .set('daycareGroupId', daycareGroupId.toString())
      .set('date', date)
    return this.http.get<DailyGroupAttendance>(this.resourceUrl, { params: params })
  }

  submitMonthlyAttendanceForChild(attendance: MonthlyChildAttendance, month: string, year: number) {
    const attendanceUpdateRuquest = {
      datesToMarkAsPresent: attendance.datesWhenPresent,
      datesToMarkAsAbsent: attendance.datesWhenAbsent
    }
    const params = new HttpParams()
      .set('childId', attendance.childId.toString())
      .set('month', month)
      .set('year', year.toString())
    return this.http.put(this.resourceUrl, attendanceUpdateRuquest, { params: params })
      .pipe(
        catchError(this.handleError)
      )
  }

  submitDailyAttendanceForGroup(attendance: DailyGroupAttendance) {
    const attendanceRequest = {
      date: attendance.date.toString(),
      idsOfChildrenToMarkAsPresent: attendance.presentChildrenIds,
      idsOfChildrenToMarkAsAbsent: attendance.absentChildrenIds
    }
    const params = new HttpParams()
      .set('daycareGroupId', attendance.daycareGroupId.toString())
    return this.http.put(this.resourceUrl, attendanceRequest, { params: params })
      .pipe(
        catchError(this.handleError)
      )
  }

}

