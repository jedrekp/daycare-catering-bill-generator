import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { UnprocessableEntityError } from 'src/app/common/error/unprocessable-entity-error';
import { CateringBillOperationsService } from 'src/app/service/data/catering-bill-operations.service';
import { CateringBill, DailyCateringOrder } from '../catering-bill';

@Component({
  selector: 'app-bill-preview',
  templateUrl: './bill-preview.component.html',
  styleUrls: ['./bill-preview.component.scss']
})
export class BillPreviewComponent implements OnInit {

  cateringBillPreview: CateringBill
  tableDataSource: MatTableDataSource<DailyCateringOrder>
  displayedColumns: string[] = ['orderDate', 'optionName', 'price']

  constructor(
    public dialogRef: MatDialogRef<BillPreviewComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private cateringBillsOperationsService: CateringBillOperationsService,
    private snackbar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.generateBillPreview(this.data.childId, this.data.month, this.data.year)
  }

  generateBillPreview(childId: number, month: string, year: number) {
    this.cateringBillsOperationsService.getBillPreview(childId, month, year).subscribe(
      bill => {
        this.cateringBillPreview = bill
        this.tableDataSource = new MatTableDataSource(this.cateringBillPreview.dailyCateringOrders)
      },
      err => {
        this.dialogRef.close()
        if (err instanceof UnprocessableEntityError)
          this.snackbar.open(err.originalError?.error, null, { panelClass: 'dcbg-error-snackbar' })
        else
          throw err
      }
    )
  }

}
