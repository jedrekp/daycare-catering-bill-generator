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
    return this.httpClient.get<DailyGroupAttendance>('http://localhost:8081/dailyAttendances', { params: params })
  }

  submitDailyGroupAttendance(dailyGroupAttendance: DailyGroupAttendance) {
    return this.httpClient.post<DailyGroupAttendance>('http://localhost:8081/dailyAttendances', dailyGroupAttendance)
  }

  retrieveMonthlyAttendanceForChild(childId: number, month: string, year: number) {
    let params = new HttpParams()
      .set('month', month)
      .set('year', year.toString())
    return this.httpClient.get<MonthlyChildAttendance>(`http://localhost:8081/dailyAttendances/children/${childId}`, { params: params })
  }

}
