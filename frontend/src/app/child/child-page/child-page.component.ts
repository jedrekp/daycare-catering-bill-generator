import { Component, OnInit } from '@angular/core';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { ActivatedRoute } from '@angular/router';
import { ChildDataService } from '../child-data.service';
import { Child, AssignedOption } from '../child';
import { ChildCreateEditComponent } from '../child-create-edit/child-create-edit.component';
import { ChildAssignOptionComponent } from '../child-assign-option/child-assign-option.component';
import { DaycareGroupDataService } from 'src/app/daycare-group/daycare-group-data.service';
import { AssignToGroupComponent } from 'src/app/daycare-group/assign-to-group/assign-to-group.component';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { CONFIRMATION_HEADER, ACTION_COMPLETED_HEADER, ERROR_HEADER } from 'src/app/const';
import { AttendanceDataService } from 'src/app/attendance/attendance-data.service';
import { MonthlyChildAttendance } from 'src/app/attendance/monthly-child-attandance';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-child-page',
  templateUrl: './child-page.component.html',
  styleUrls: ['./child-page.component.css']
})
export class ChildPageComponent implements OnInit {

  private modalRef: BsModalRef
  private child: Child
  private firstDayOfSelectedAttendanceMonth: Date
  private weekdaysFromSelectedMonth: Date[]
  private minDate: Date
  private monthlyChildAttendance: MonthlyChildAttendance
  private calendarSlicePoint: number;

  constructor(
    private route: ActivatedRoute,
    private datePipe: DatePipe,
    private bsModalService: BsModalService,
    private dialogModalService: DialogModalService,
    private childDataService: ChildDataService,
    private daycareGroupDataService: DaycareGroupDataService,
    private attendanceDataService: AttendanceDataService
  ) { }

  ngOnInit() {
    this.child = new Child(-1, '', '', '', false)
    this.monthlyChildAttendance = new MonthlyChildAttendance("JANUARY", 2019, -1)
    this.minDate = new Date(2019, 0, 1)
    let currentDate = new Date()
    this.firstDayOfSelectedAttendanceMonth = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1)
    this.retrieveChild(this.route.snapshot.params['childId'])
  }

  retrieveChild(childId: number) {
    this.childDataService.retrieveChild(childId).subscribe(
      child => {
        this.child = child
        this.retrieveAttendance()
      })
  }

  openEditChildModal() {
    let initialState = {
      child: new Child(this.child.id, this.child.firstName, this.child.lastName,
        this.child.parentEmail, this.child.archived)
    };
    this.modalRef = this.bsModalService.show(ChildCreateEditComponent,
      { class: 'modal-top-10 modal-sm', initialState, ignoreBackdropClick: true })
    this.modalRef.content.onClose.subscribe(
      childId => {
        if (childId) {
          this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
            `Child #${this.child.id} has been succesfully edited.`)
          this.modalRef.content.onClose.subscribe(
            onClose => {
              this.retrieveChild(this.child.id)
            })
        }
      })
  }

  openAssignChildToGroupModal() {
    let initialState = { childId: this.child.id }
    this.modalRef = this.bsModalService.show(AssignToGroupComponent,
      { class: 'modal-top-10 modal-sm', initialState, ignoreBackdropClick: true })
    this.modalRef.content.onClose.subscribe(
      daycareGroup => {
        if (daycareGroup) {
          this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
            `Child #${this.child.id} is now assigned to daycare group #${daycareGroup.id} (${daycareGroup.groupName}).`)
          this.modalRef.content.onClose.subscribe(
            onClose => {
              this.retrieveChild(this.child.id)
            })
        }
      })
  }

  removeFromGroup() {
    this.modalRef = this.dialogModalService.openConfirmationModal(CONFIRMATION_HEADER,
      `You are about to remove child #${this.child.id} from daycare group #${this.child.daycareGroup.id} (${this.child.daycareGroup.groupName}).`)
    this.modalRef.content.onClose.subscribe(
      onClose => {
        if (onClose) {
          this.daycareGroupDataService.removeChildFromDaycareGroup(this.child.daycareGroup.id, this.child.id).subscribe(
            response => {
              this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
                `Child# ${this.child.id} is no longer assigned to daycare group #${this.child.daycareGroup.id} (${this.child.daycareGroup.groupName}).`)
              this.modalRef.content.onClose.subscribe(
                onclose => {
                  this.retrieveChild(this.child.id)
                })
            },
            err => {
              this.dialogModalService.openInformationModal(ERROR_HEADER, err.message)
            })
        }
      })
  }

  openAssignNewOptionModal() {
    let initialState = { childId: this.child.id }
    this.modalRef = this.bsModalService.show(ChildAssignOptionComponent,
      { class: 'modal-top-10 modal-sm', initialState, ignoreBackdropClick: true })
    this.modalRef.content.onClose.subscribe(
      cateringOption => {
        if (cateringOption) {
          this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
            `Catering option #${cateringOption.id} (${cateringOption.optionName}) has been succesfully assigned to child #${this.child.id}.`)
          this.modalRef.content.onClose.subscribe(
            onClose => {
              this.retrieveChild(this.child.id)
            })
        }
      })
  }

  removeAssignedOption(assignedOption: AssignedOption) {
    this.modalRef = this.dialogModalService.openConfirmationModal(CONFIRMATION_HEADER,
      `You are about to remove catering option #${assignedOption.cateringOption.id} (${assignedOption.cateringOption.optionName}) from child #${this.child.id}.\n
      This change may affect catering bills generated in future, for months when this option is in effect.\n
      It will not affect catering bills that have already been generated.`)
    this.modalRef.content.onClose.subscribe(
      onClose => {
        if (onClose) {
          this.childDataService.removeAssignedOptionFromChild(this.child.id, assignedOption.id).subscribe(
            response => {
              this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,
                `Catering option#${assignedOption.cateringOption.id} (${assignedOption.cateringOption.optionName}) is no longer assigned to child #${this.child.id}.`)
              this.retrieveChild(this.child.id);
            },
            err => {
              this.dialogModalService.openInformationModal(ERROR_HEADER, err.message)
            })
        }
      })
  }

  onOpenCalendar(container) {
    container.monthSelectHandler = (event: any): void => {
      container._store.dispatch(container._actions.select(event.date))
    }
    container.setViewMode('month')
  }

  getAllWeekdaysFromSelectedMonth() {
    let date = new Date(this.firstDayOfSelectedAttendanceMonth)
    let month = date.getMonth()
    this.weekdaysFromSelectedMonth = []
    while (date.getMonth() === month) {
      if (!(date.getDay() == 0 || date.getDay() == 6)) {
        this.weekdaysFromSelectedMonth.push(new Date(date));
      }
      date.setDate(date.getDate() + 1);
    }
    this.calendarSlicePoint = Math.ceil(this.weekdaysFromSelectedMonth.length / 2)
  }

  retrieveAttendance() {
    this.getAllWeekdaysFromSelectedMonth()
    let monthString = this.datePipe.transform(this.firstDayOfSelectedAttendanceMonth, 'LLLL').toUpperCase()
    let year = this.firstDayOfSelectedAttendanceMonth.getFullYear()
    this.attendanceDataService.retrieveMonthlyAttendanceForChild(this.child.id, monthString, year).subscribe(
      monthlyChildAttendance => {
        this.monthlyChildAttendance = monthlyChildAttendance
      },
      err => {
        this.dialogModalService.openInformationModal(ERROR_HEADER, err.message)
      })

  }

  checkAttendanceStatus(date: Date) {
    let dateAsString = this.datePipe.transform(date, 'yyyy-MM-dd')
    if (this.monthlyChildAttendance.daysWhenPresent.indexOf(dateAsString) > -1) {
      return 1
    } else if (this.monthlyChildAttendance.daysWhenAbsent.indexOf(dateAsString) > -1) {
      return 0
    } else {
      return -1
    }
  }

  adjustDailyAttendance(date: Date, optionValue: number) {
    let dateAsString = this.datePipe.transform(date, 'yyyy-MM-dd')
    if (optionValue == 1) {
      let absentIndex = this.monthlyChildAttendance.daysWhenAbsent.indexOf(dateAsString)
      if (absentIndex > -1) {
        this.monthlyChildAttendance.daysWhenAbsent.splice(absentIndex, 1)
      }
      this.monthlyChildAttendance.daysWhenPresent.push(dateAsString)
    } else if (optionValue == 0) {
      let presentIndex = this.monthlyChildAttendance.daysWhenPresent.indexOf(dateAsString)
      if (presentIndex > -1) {
        this.monthlyChildAttendance.daysWhenPresent.splice(presentIndex, 1)
      }
      this.monthlyChildAttendance.daysWhenAbsent.push(dateAsString)
    }
  }

}
