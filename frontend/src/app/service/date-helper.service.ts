import { DatePipe } from '@angular/common';
import { Injectable } from '@angular/core';
import { months } from 'moment';
import { monthNames } from 'src/const';

@Injectable({
  providedIn: 'root'
})
export class DateHelperService {

  constructor(
    private datePipe: DatePipe
  ) { }

  getMonthAsString(date: Date) {
    return this.datePipe.transform(date, 'LLLL')
  }

  getDateAsString(date: Date) {
    return this.datePipe.transform(date, 'yyyy-MM-dd')
  }

  getMonthAsNumber(month: string) {
    const index = monthNames.indexOf(month)
    if (index < 0)
      return null
    if(index == 11)
      return
  }

  getAllWeekdaysFromMonth(firstDayOfMonth: Date) {
    const date = new Date(firstDayOfMonth)
    const month = firstDayOfMonth.getMonth()
    const weekdaysFromSelectedMonth = []
    while (date.getMonth() === month) {
      if (this.isWeekDay(date))
        weekdaysFromSelectedMonth.push(new Date(date))
      date.setDate(date.getDate() + 1)
    }
    return weekdaysFromSelectedMonth
  }

  getCurrentDateOrMondayIfWeekend(): Date {
    let date = new Date();
    if (date.getDay() === 0) {
      date.setDate(date.getDate() + 1)
    } else if (date.getDay() === 6) {
      date.setDate(date.getDate() + 2)
    }
    return date
  }

  isWeekDay(date: Date) {
    const day = date.getDay();
    return day !== 0 && day !== 6;
  }

  getNextWeekday(date: Date) {
    const day = date.getDay()
    const nextWeekday = new Date(date)

    if (day == 5)
      nextWeekday.setDate(nextWeekday.getDate() + 3)
    else if (day == 6)
      nextWeekday.setDate(nextWeekday.getDate() + 2)
    else
      nextWeekday.setDate(nextWeekday.getDate() + 1)

    return nextWeekday
  }

  getPreviousWeekday(date: Date) {
    const day = date.getDay()
    const previousWeekday = new Date(date)

    if (day == 1)
      previousWeekday.setDate(previousWeekday.getDate() - 3)
    else if (day == 0)
      previousWeekday.setDate(previousWeekday.getDate() - 2)
    else
      previousWeekday.setDate(previousWeekday.getDate() - 1)

    return previousWeekday
  }

  getPreviousMonthAsString(month: string) {
    const index = monthNames.indexOf(month)
    if (index < 0)
      return null
    if (index == 0)
      return monthNames[11]

    return monthNames[index - 1]
  }

  getNextMonthAsString(month: string) {
    const index = monthNames.indexOf(month)
    if (index < 0)
      return null
    if (index == 11)
      return monthNames[0]

    return monthNames[index + 1]
  }

}
