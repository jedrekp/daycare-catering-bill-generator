import { Component, Input, OnChanges } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Child } from 'src/app/child/child';
import { DaycareGroupDataService } from 'src/app/service/data/daycare-group-data.service';
import { DateHelperService } from 'src/app/service/date-helper.service';
import { monthNames } from 'src/const';
import { BillPreviewComponent } from '../bill-preview/bill-preview.component';
import { CateringBill } from '../catering-bill';
import { CateringBillDisplayComponent } from '../catering-bill-display/catering-bill-display.component';
import { SelectedMonthAndGroup } from '../select-month-and-group/select-month-and-group.component';
import { SendingBillComponent } from '../sending-bill.component/sending-bill.component';

@Component({
  selector: 'app-catering-bills-list',
  templateUrl: './catering-bills-list.component.html',
  styleUrls: ['./catering-bills-list.component.scss']
})
export class CateringBillsListComponent implements OnChanges {

  @Input('selectedMonthAndGroup') selectedMonthAndGroup: SelectedMonthAndGroup

  children: Child[]
  cateringBills: CateringBill[]
  tableDataSource: MatTableDataSource<Child>
  displayedColumns: string[] = ['childId', 'firstName', 'lastName', 'generateBill', 'displayBill']
  monthNamesArray = monthNames

  constructor(
    private daycareGroupDataService: DaycareGroupDataService,
    private dateHelperService: DateHelperService,
    private dialog: MatDialog
  ) { }

  ngOnChanges(): void {
    if (this.selectedMonthAndGroup) {
      this.retrieveChildren()
      this.retrieveCateringBills()
    }
  }

  retrieveChildren() {
    this.children = null
    this.daycareGroupDataService.getAllChildrenFromGroup(this.selectedMonthAndGroup.group.id).subscribe(
      children => {
        this.children = children
        this.tableDataSource = new MatTableDataSource(this.children)
      })
  }

  retrieveCateringBills() {
    this.cateringBills = null
    this.daycareGroupDataService.getBillsFromSpecificMonthForAllChildrenInGroup(
      this.selectedMonthAndGroup.group.id,
      this.selectedMonthAndGroup.month.toUpperCase(),
      this.selectedMonthAndGroup.year).subscribe(
        bills => this.cateringBills = bills
      )
  }

  previousMonth() {
    const previous = this.dateHelperService.getPreviousMonthAsString(this.selectedMonthAndGroup.month)
    this.selectedMonthAndGroup.month = previous
    if (previous == "December")
      this.selectedMonthAndGroup.year = this.selectedMonthAndGroup.year - 1
    this.retrieveCateringBills()
  }

  nextMonth() {
    const next = this.dateHelperService.getNextMonthAsString(this.selectedMonthAndGroup.month)
    this.selectedMonthAndGroup.month = next
    if (next == "January")
      this.selectedMonthAndGroup.year = this.selectedMonthAndGroup.year + 1
    this.retrieveCateringBills()
  }

  checkIfBillExists(childId: number) {
    return this.cateringBills.filter(cateringBill => cateringBill.childId == childId).length > 0
  }

  openBillPreview(childId: number) {
    const dialogRef = this.dialog.open(BillPreviewComponent, {
      disableClose: true,
      panelClass: 'catering-bill-dialog',
      data:
      {
        childId: childId,
        month: this.selectedMonthAndGroup.month.toUpperCase(),
        year: this.selectedMonthAndGroup.year
      }
    })

    dialogRef.afterClosed().subscribe(
      bill => {
        if (bill)
          this.saveAndSendEmail(bill)
      })
  }

  displayCateringBill(childId: number) {
    const bill = this.cateringBills.filter(cateringBill => cateringBill.childId == childId)[0]

    const dialogRef = this.dialog.open(CateringBillDisplayComponent, {
      disableClose: true,
      panelClass: 'catering-bill-dialog',
      data:
      {
        cateringBill: bill
      }
    })

    dialogRef.afterClosed().subscribe(
      bill => {
        if (bill)
          this.sendEmail(bill)
      })
  }

  saveAndSendEmail(bill: CateringBill) {
    const dialogRef = this.dialog.open(SendingBillComponent,
      {
        disableClose: true,
        panelClass: 'dcbg-dialog',
        data:
        {
          cateringBill: bill,
          newBill: true
        }
      })

    dialogRef.afterClosed().subscribe(
      () => this.retrieveCateringBills()
    )
  }

  sendEmail(bill: CateringBill) {
    const dialogRef = this.dialog.open(SendingBillComponent,
      {
        disableClose: true,
        panelClass: 'dcbg-dialog',
        data:
        {
          cateringBill: bill,
          newBill: false
        }
      })

    dialogRef.afterClosed().subscribe(
      () => this.retrieveCateringBills()
    )
  }

}
