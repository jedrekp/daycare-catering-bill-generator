import { Component, Input, OnChanges } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { delay } from 'rxjs/operators';
import { Child } from 'src/app/child/child';
import { AttendanceDataService } from 'src/app/service/data/attendance-data.service';
import { DaycareGroupDataService } from 'src/app/service/data/daycare-group-data.service';
import { DateHelperService } from 'src/app/service/date-helper.service';
import { DailyGroupAttendance } from '../daily-group-attendance';
import { SelectedDateAndGroup } from '../select-date-and-group/select-date-and-group.component';

@Component({
  selector: 'app-daycare-group-daily-attendance',
  templateUrl: './daycare-group-daily-attendance.component.html',
  styleUrls: ['./daycare-group-daily-attendance.component.scss']
})
export class DaycareGroupDailyAttendanceComponent implements OnChanges {

  @Input('selectedDateAndGroup') selectedDateAndGroup: SelectedDateAndGroup

  children: Child[]
  dailyGroupAttendance: DailyGroupAttendance
  tableDataSource: MatTableDataSource<Child>
  displayedColumns: string[] = ['childId', 'firstName', 'lastName', 'present', 'absent']

  constructor(
    private daycareGroupDataService: DaycareGroupDataService,
    private attendanceDataService: AttendanceDataService,
    private dateHelperService: DateHelperService,
    private snackbar: MatSnackBar
  ) { }

  ngOnChanges(): void {
    if (this.selectedDateAndGroup) {
      this.retrieveAttendance()
      this.retrieveChildrenFromGroup(this.selectedDateAndGroup.group.id)
    }
  }

  retrieveAttendance() {
    this.dailyGroupAttendance = null
    const dateString = this.dateHelperService.getDateAsString(this.selectedDateAndGroup.date)
    this.attendanceDataService.getDailyAttendanceForGroup(this.selectedDateAndGroup.group.id, dateString).subscribe(
      attendance => this.dailyGroupAttendance = attendance
    )
  }

  retrieveChildrenFromGroup(groupId: number) {
    this.children = null
    this.daycareGroupDataService.getAllChildrenFromGroup(groupId).subscribe(
      children => {
        this.children = children
        this.tableDataSource = new MatTableDataSource(children)
      }
    )
  }

  nextWeekday() {
    this.selectedDateAndGroup.date = this.dateHelperService.getNextWeekday(this.selectedDateAndGroup.date)
    this.retrieveAttendance()
  }

  previousWeekday() {
    this.selectedDateAndGroup.date = this.dateHelperService.getPreviousWeekday(this.selectedDateAndGroup.date)
    this.retrieveAttendance()
  }

  submitAttendance() {
    if (this.allChildrenMarked())
      this.attendanceDataService.submitDailyAttendanceForGroup(this.dailyGroupAttendance).subscribe(
        () => {
          this.snackbar.open('Attendance has been successfully submited.')
        }
      )
    else (
      this.snackbar.open('Attendance must be tracked for all children before it can be submited', null, { panelClass: 'dcbg-error-snackbar' })
    )
  }

  isPresent(childId: number) {
    return this.dailyGroupAttendance.presentChildrenIds.indexOf(childId) > -1
  }

  isAbsent(childId: number) {
    return this.dailyGroupAttendance.absentChildrenIds.indexOf(childId) > -1
  }

  setAttendanceForAllChildren(isPresent: boolean) {
    this.children.map(child => child.id).forEach(childId => this.setAttendanceStatus(childId, isPresent))
  }

  setAttendanceStatus(childId: number, isPresent: boolean) {
    const presentIndex = this.dailyGroupAttendance.presentChildrenIds.indexOf(childId)
    const absentIndex = this.dailyGroupAttendance.absentChildrenIds.indexOf(childId)
    if (isPresent)
      this.setToPresent(childId, presentIndex, absentIndex)
    else
      this.setToAbsent(childId, presentIndex, absentIndex)
  }

  setToPresent(childId: number, presentIndex: number, absentIndex: number) {
    if (presentIndex == -1)
      this.dailyGroupAttendance.presentChildrenIds.push(childId)
    if (absentIndex > -1)
      this.dailyGroupAttendance.absentChildrenIds.splice(absentIndex, 1)
  }

  setToAbsent(childId: number, presentIndex: number, absentIndex: number) {
    if (presentIndex > -1)
      this.dailyGroupAttendance.presentChildrenIds.splice(presentIndex, 1)
    if (absentIndex == -1)
      this.dailyGroupAttendance.absentChildrenIds.push(childId)
  }

  allChildrenMarked() {
    return this.children
      .map(child => child.id)
      .filter(childId => !this.isPresent(childId))
      .filter(childId => !this.isAbsent(childId))
      .length == 0
  }

}
