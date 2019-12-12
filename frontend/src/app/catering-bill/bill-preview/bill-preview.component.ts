import { Component, OnInit, Input } from '@angular/core';
import { CateringBill } from '../catering-bill';
import { BsModalRef } from 'ngx-bootstrap/modal/';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { CateringBillOperationsService } from '../catering-bill-operations.service';

@Component({
  selector: 'app-bill-preview',
  templateUrl: './bill-preview.component.html',
  styleUrls: ['./bill-preview.component.css']
})
export class BillPreviewComponent implements OnInit {

  @Input() private childId: number
  @Input() private month: string
  @Input() private year: number
  private cateringBill: CateringBill

  constructor(
    private modalRef: BsModalRef,
    private dialogModalService: DialogModalService,
    private cateringBillOperationsService: CateringBillOperationsService
  ) { }

  ngOnInit() {
    this.getCateringBillPreview(this.childId, this.month, this.year)
  }

  getCateringBillPreview(childId: number, month: string, year: number) {
    this.cateringBillOperationsService.getCateringBillPreview(childId, month, year).subscribe(
      cateringBill => {
        this.cateringBill = cateringBill
      })
  }

  saveBillAndSendEmailToParent() {

  }

  onCancel() {
    this.modalRef.hide()
  }

}
