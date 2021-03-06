import { Injectable } from '@angular/core';
import { DailyGroupAttendance } from './daily-group-attendance';
import { HttpClient, HttpParams } from '@angular/common/http';
import { MonthlyChildAttendance } from './monthly-child-attandance';
import { API_URL } from '../const';


@Injectable({
  providedIn: 'root'
})
export class AttendanceDataService {

  constructor(
    private httpClient: HttpClient
  ) { }

  retrieveDailyAttendanceForGroup(daycareGroupId: number, date: string) {
    let params = new HttpParams()
      .set('daycareGroupId', daycareGroupId.toString())
      .set('date', date)
    return this.httpClient.get<DailyGroupAttendance>(`${API_URL}/attendanceSheets`, { params: params })
  }

  submitDailyGroupAttendance(daycareGroupId: number, attendance: DailyGroupAttendance) {
    let attendanceRequest = new TrackDailyGroupAttendanceRequest(attendance.date.toString(), attendance.presentChildrenIds, attendance.absentChildrenIds)
    let params = new HttpParams()
      .set('daycareGroupId', daycareGroupId.toString())
    return this.httpClient.put(`${API_URL}/attendanceSheets`, attendanceRequest, { params: params })
  }

  retrieveMonthlyAttendanceForChild(childId: number, month: string, year: number) {
    let params = new HttpParams()
      .set('childId', childId.toString())
      .set('month', month)
      .set('year', year.toString())
    return this.httpClient.get<MonthlyChildAttendance>(`${API_URL}/attendanceSheets`, { params: params })
  }

  submitMonthlyAttendanceForChild(childId: number, month: string, year: number, attendance: MonthlyChildAttendance) {
    let attendanceRequest = new UpdateMonthlyAttendanceForChildRequest(attendance.datesWhenPresent, attendance.datesWhenAbsent)
    let params = new HttpParams()
      .set('childId', childId.toString())
      .set('month', month)
      .set('year', year.toString())
    return this.httpClient.put(`${API_URL}/attendanceSheets`, attendanceRequest, { params: params })
  }

}

class TrackDailyGroupAttendanceRequest {
  constructor(
    private date: String,
    private idsOfChildrenToMarkAsPresent: number[],
    private idsOfChildrenToMarkAsAbsent: number[]
  ) { }
}

class UpdateMonthlyAttendanceForChildRequest {
  constructor(
    private datesToMarkAsPresent: string[],
    private datesToMarkAsAbsent: string[]
  ) { }
}