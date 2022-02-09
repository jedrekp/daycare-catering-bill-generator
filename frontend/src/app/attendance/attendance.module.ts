import { NgModule } from '@angular/core';
import { SharedModule } from '../shared.module';
import { GroupAttendanceMainComponent } from './track-group-attendance/group-attendance-main.component';
import { SelectDateAndGroupComponent } from './select-date-and-group/select-date-and-group.component';
import { DaycareGroupDailyAttendanceComponent } from './daycare-group-daily-attendance/daycare-group-daily-attendance.component';
import { SingleChildAttendanceComponent } from './single-child-attendance/single-child-attendance.component';



@NgModule({
  declarations: [
    GroupAttendanceMainComponent,
    SelectDateAndGroupComponent,
    DaycareGroupDailyAttendanceComponent,
    SingleChildAttendanceComponent
  ],
  imports: [
    SharedModule
  ],
  exports: [
    SingleChildAttendanceComponent
  ]
})
export class AttendanceModule { }