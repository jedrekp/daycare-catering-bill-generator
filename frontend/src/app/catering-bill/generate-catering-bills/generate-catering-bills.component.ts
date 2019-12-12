import { Component, OnInit } from '@angular/core';
import { DaycareGroup } from 'src/app/daycare-group/daycare-group';
import { DaycareGroupDataService } from 'src/app/daycare-group/daycare-group-data.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { CateringBill } from '../catering-bill';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-generate-catering-bills',
  templateUrl: './generate-catering-bills.component.html',
  styleUrls: ['./generate-catering-bills.component.css']
})
export class GenerateCateringBillsComponent implements OnInit {

  private minDate: Date
  private selectMonthAndGroupForm: FormGroup
  private daycareGroups: DaycareGroup[]
  private selectedDaycareGroup: DaycareGroup
  private cateringBills: CateringBill[] = []
  private selectedMonth: string

  constructor(
    private datePipe: DatePipe,
    private daycareGroupDataService: DaycareGroupDataService
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
      }
    )
  }

  onSelect() {
    let dateFromSelectedMonth = this.selectMonthAndGroupForm.get('firstDayOfSelectedMonth').value
    this.selectedMonth = this.datePipe.transform(dateFromSelectedMonth, 'MMMM yyyy')
    this.daycareGroupDataService.retrieveSingleDaycareGroup(this.selectMonthAndGroupForm.get('daycareGroup').value.id)
      .subscribe(
        daycareGroup => {
          this.selectedDaycareGroup = daycareGroup
          this.daycareGroupDataService.retrieveBillsForSpecificMonthForAllChildrenInGroup(
            daycareGroup.id,
            this.datePipe.transform(dateFromSelectedMonth, 'LLLL').toUpperCase(),
            dateFromSelectedMonth.getFullYear()
          ).subscribe(
            cateringBills => {
              this.cateringBills = cateringBills
            })
        })
  }

  checkIfCateringBillExistsForChild(childId: number) {
    return this.cateringBills.filter(cateringBill => cateringBill.childId == childId).length > 0
  }

}
