import { Injectable } from '@angular/core';
import { DailyGroupAttendance } from './daily-group-attendance';
import { HttpClient, HttpParams } from '@angular/common/http';
import { MonthlyChildAttendance } from './monthly-child-attandance';

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
    return this.httpClient.get<DailyGroupAttendance>('http://localhost:8081/attendanceSheets', { params: params })
  }

  submitDailyGroupAttendance(daycareGroupId: number, attendance: DailyGroupAttendance) {
    let params = new HttpParams()
      .set('daycareGroupId', daycareGroupId.toString())
    return this.httpClient.post<any>('http://localhost:8081/attendanceSheets', attendance, { params: params })
  }

  retrieveMonthlyAttendanceForChild(childId: number, month: string, year: number) {
    let params = new HttpParams()
      .set('childId', childId.toString())
      .set('month', month)
      .set('year', year.toString())
    return this.httpClient.get<MonthlyChildAttendance>(`http://localhost:8081/attendanceSheets`, { params: params })
  }

  submitMonthlyAttendanceForChild(childId: number, month: string, year: number, attendance: MonthlyChildAttendance) {
    let params = new HttpParams()
      .set('childId', childId.toString())
      .set('month', month)
      .set('year', year.toString())
    return this.httpClient.put<any>(`http://localhost:8081/attendanceSheets`, attendance, { params: params })
  }

}
