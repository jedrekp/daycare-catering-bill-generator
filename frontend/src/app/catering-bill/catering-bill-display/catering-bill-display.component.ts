import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { CateringBill, DailyCateringOrder } from '../catering-bill';

@Component({
  selector: 'app-catering-bill-display',
  templateUrl: './catering-bill-display.component.html',
  styleUrls: ['./catering-bill-display.component.scss']
})
export class CateringBillDisplayComponent implements OnInit {

  cateringBill: CateringBill
  tableDataSource: MatTableDataSource<DailyCateringOrder>
  displayedColumns: string[] = ['orderDate', 'optionName', 'price']

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  ngOnInit(): void {
    this.cateringBill = this.data.cateringBill
    this.tableDataSource = new MatTableDataSource(this.cateringBill.dailyCateringOrders)
  }

}