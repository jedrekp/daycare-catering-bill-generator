import { Injectable } from '@angular/core';
import { DailyAttendanceDTO } from './daily-attendance-DTO';
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
    return this.httpClient.get<DailyAttendanceDTO>('http://localhost:8081/dailyAttendances', { params: params })
  }

}
