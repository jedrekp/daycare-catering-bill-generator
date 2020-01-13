import { Component, OnInit } from '@angular/core';
import { DaycareGroup } from 'src/app/daycare-group/daycare-group';
import { DaycareGroupDataService } from 'src/app/daycare-group/daycare-group-data.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { CateringBill } from '../catering-bill';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { BillPreviewComponent } from '../bill-preview/bill-preview.component';
import { BillDisplayComponent } from '../bill-display/bill-display.component';
import { ErrorHandlerService } from 'src/app/error/error-handler.service';

@Component({
  selector: 'app-generate-catering-bills',
  templateUrl: './generate-catering-bills.component.html',
  styleUrls: ['./generate-catering-bills.component.css']
})
export class GenerateCateringBillsComponent implements OnInit {

  private modalRef: BsModalRef
  private minDate: Date
  private selectMonthAndGroupForm: FormGroup
  private daycareGroups: DaycareGroup[]
  private selectedDaycareGroup: DaycareGroup
  private cateringBills: CateringBill[]
  private dateFromSelectedMonth: Date

  constructor(
    private bsModalService: BsModalService,
    private datePipe: DatePipe,
    private daycareGroupDataService: DaycareGroupDataService,
    private errorHandlerService: ErrorHandlerService
  ) { }

  ngOnInit() {
    this.minDate = new Date(2019, 0, 1)
    this.selectMonthAndGroupForm = new FormGroup({
      daycareGroup: new FormControl(null, [Validators.required]),
      firstDayOfSelectedMonth: new FormControl(null, Validators.required),
    })
    let currentDate = new Date()
    this.selectMonthAndGroupForm.patchValue({ firstDayOfSelectedMonth: new Date(currentDate.getFullYear(), currentDate.getMonth(), 1) })
    this.retrieveDaycareGroups()
  }

  onOpenCalendar(container) {
    container.monthSelectHandler = (event: any): void => {
      container._store.dispatch(container._actions.select(event.date))
    }
    container.setViewMode('month')
  }

  retrieveDaycareGroups() {
    this.daycareGroupDataService.retrieveDaycareGroups().subscribe(
      daycareGroups => {
        this.daycareGroups = daycareGroups
        if (daycareGroups.length > 0) {
          this.selectMonthAndGroupForm.patchValue({ daycareGroup: daycareGroups[0] })
        }
      },
      err => {
        this.errorHandlerService.redirectToErrorPage(err)
      })
  }

  retrieveChildrenAndBillInfo() {
    this.dateFromSelectedMonth = this.selectMonthAndGroupForm.get('firstDayOfSelectedMonth').value
    this.daycareGroupDataService.retrieveSingleDaycareGroup(this.selectMonthAndGroupForm.get('daycareGroup').value.id)
      .subscribe(
        daycareGroup => {
          this.selectedDaycareGroup = daycareGroup
          this.daycareGroupDataService.retrieveBillsForSpecificMonthForAllChildrenInGroup(
            daycareGroup.id,
            this.datePipe.transform(this.dateFromSelectedMonth, 'LLLL').toUpperCase(),
            this.dateFromSelectedMonth.getFullYear()
          ).subscribe(
            cateringBills => {
              this.cateringBills = cateringBills
            },
            err => {
              this.errorHandlerService.redirectToErrorPage(err)
            })
        },
        err => {
          this.errorHandlerService.redirectToErrorPage(err)
        })
  }

  checkIfCateringBillExistsForChild(childId: number) {
    return this.cateringBills.filter(cateringBill => cateringBill.childId == childId).length > 0
  }

  displayBillPreview(childId: number) {
    let initialState = {
      childId: childId,
      month: this.datePipe.transform(this.dateFromSelectedMonth, 'LLLL').toUpperCase(),
      year: this.dateFromSelectedMonth.getFullYear()
    }
    this.modalRef = this.bsModalService.show(BillPreviewComponent,
      { class: 'modal-md', initialState, ignoreBackdropClick: true, keyboard: false })

    this.modalRef.content.onClose.subscribe(
      onClose => {
        if (onClose) {
          this.retrieveChildrenAndBillInfo()
        }
      })
  }

  displayCateringBill(childId: number) {
    let initialState = {
      cateringBill: this.cateringBills.filter(cateringBill => cateringBill.childId == childId)[0]
    }
    this.modalRef = this.bsModalService.show(BillDisplayComponent,
      { class: 'modal-md', initialState, ignoreBackdropClick: true, keyboard: false })
  }

}
