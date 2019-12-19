import { Component, OnInit, Input } from '@angular/core';
import { CateringBill } from '../catering-bill';
import { BsModalRef } from 'ngx-bootstrap/modal/';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { CateringBillOperationsService } from '../catering-bill-operations.service';
import { ChildDataService } from 'src/app/child/child-data.service';
import { ERROR_HEADER } from 'src/app/const';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-bill-preview',
  templateUrl: './bill-preview.component.html',
  styleUrls: ['./bill-preview.component.css']
})
export class BillPreviewComponent implements OnInit {

  @Input() private childId: number
  @Input() private month: string
  @Input() private year: number
  private onClose: Subject<boolean>
  private cateringBill: CateringBill

  constructor(
    private modalRef: BsModalRef,
    private dialogModalService: DialogModalService,
    private cateringBillOperationsService: CateringBillOperationsService,
    private childDataService: ChildDataService,
  ) { }

  ngOnInit() {
    this.onClose = new Subject<boolean>()
    this.getCateringBillPreview(this.childId, this.month, this.year)
  }

  getCateringBillPreview(childId: number, month: string, year: number) {
    this.cateringBillOperationsService.getCateringBillPreview(childId, month, year).subscribe(
      cateringBill => {
        this.cateringBill = cateringBill
      })
  }

  saveBillAndSendEmailToParent() {
    this.childDataService.saveCateringBill(this.cateringBill.childId, this.cateringBill.month.toUpperCase(),
      this.cateringBill.year, this.cateringBill.dailyCateringOrders).subscribe(
        cateringBill => {
          this.cateringBillOperationsService.sendCateringBillToParent(cateringBill.id).subscribe(
            response => {
              this.modalRef.hide()
              this.onClose.next(true)
            })
        },
        err => {
          this.dialogModalService.openNestedInformationModal(ERROR_HEADER, err.msg)
        })
  }

  onCancel() {
    this.modalRef.hide()
    this.onClose.next(false)
  }

}
