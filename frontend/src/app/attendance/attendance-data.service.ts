import { Injectable } from '@angular/core';
import { DailyAttendance } from './daily-attendance';
import { HttpClient, HttpParams } from '@angular/common/http';

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
    return this.httpClient.get<DailyAttendance>('http://localhost:8081/dailyAttendances', { params: params })
  }

  submitAttendance(dailyAttendance: DailyAttendance) {
    return this.httpClient.post<DailyAttendance>('http://localhost:8081/dailyAttendances', dailyAttendance)
  }

}
