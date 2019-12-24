import { Component, OnInit, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { CateringBillOperationsService } from '../catering-bill-operations.service';
import { Subject } from 'rxjs';
import { CateringBill } from '../catering-bill';
import { ACTION_COMPLETED_HEADER, ERROR_HEADER } from 'src/app/const';

@Component({
  selector: 'app-bill-display',
  templateUrl: './bill-display.component.html',
  styleUrls: ['./bill-display.component.css']
})
export class BillDisplayComponent implements OnInit {

  @Input() private cateringBill: CateringBill
  private onClose: Subject<boolean>
  private inputDisabled: boolean = false


  constructor(
    private modalRef: BsModalRef,
    private nestedModalRef: BsModalRef,
    private dialogModalService: DialogModalService,
    private cateringBillOperationsService: CateringBillOperationsService,
  ) { }

  ngOnInit() {
    this.onClose = new Subject<boolean>()
  }

  resendBill() {
    this.inputDisabled = true
    this.cateringBillOperationsService.sendCateringBillToParent(this.cateringBill.billId).subscribe(
      response => {
        this.nestedModalRef = this.dialogModalService.openNestedInformationModal(ACTION_COMPLETED_HEADER,
          `Catering bill has been succesfully sent to parent.`)
        this.nestedModalRef.content.onClose.subscribe(
          onClose => {
            this.inputDisabled = false;
          })
      },
      err => {
        this.nestedModalRef = this.dialogModalService.openNestedInformationModal(ERROR_HEADER,
          `An issue occured when trying to send catering bill to parent. Please try again later.`)
        this.nestedModalRef.content.onClose.subscribe(
          onClose => {
            this.inputDisabled = false;
          })
      })
  }

  closeBillPage() {
    this.modalRef.hide()
    this.onClose.next(false)
  }

}
