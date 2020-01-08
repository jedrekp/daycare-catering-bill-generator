import { Component, OnInit, Input } from '@angular/core';
import { CateringBill } from '../catering-bill';
import { BsModalRef } from 'ngx-bootstrap/modal/';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { CateringBillOperationsService } from '../catering-bill-operations.service';
import { ChildDataService } from 'src/app/child/child-data.service';
import { ERROR_HEADER, CONFIRMATION_HEADER, ACTION_COMPLETED_HEADER } from 'src/app/const';
import { Subject } from 'rxjs';
import { Router } from '@angular/router';
import { ErrorHandlerService } from 'src/app/error/error-handler.service';

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
  private inputDisabled: boolean = false
  private cateringBill: CateringBill

  constructor(
    private router: Router,
    private modalRef: BsModalRef,
    private nestedModalRef: BsModalRef,
    private dialogModalService: DialogModalService,
    private cateringBillOperationsService: CateringBillOperationsService,
    private childDataService: ChildDataService,
    private errorHandlerService: ErrorHandlerService
  ) { }

  ngOnInit() {
    this.onClose = new Subject<boolean>()
    this.getCateringBillPreview(this.childId, this.month, this.year)
  }

  getCateringBillPreview(childId: number, month: string, year: number) {
    this.cateringBillOperationsService.getCateringBillPreview(childId, month, year).subscribe(
      cateringBill => {
        this.cateringBill = cateringBill
      },
      err => {
        this.modalRef.hide()
        this.onClose.next(false)
        this.errorHandlerService.redirectToErrorPage(err)
      })
  }

  submitCateringBill() {
    this.inputDisabled = true
    if (this.cateringBill.correction) {
      this.nestedModalRef = this.dialogModalService.openConfirmationModal(CONFIRMATION_HEADER, `This correction will override existing catering bill for this month.\n
      Additionally, another email will be sent to parent with new version of catering bill (It will be marked as a correction).`)
      this.nestedModalRef.content.onClose.subscribe(
        onClose => {
          if (onClose) {
            this.saveBillAndSendEmailToParent()
          } else {
            this.inputDisabled = false
          }
        })
    } else {
      this.saveBillAndSendEmailToParent()
    }
  }

  saveBillAndSendEmailToParent() {
    this.childDataService.saveCateringBill(this.cateringBill.childId, this.cateringBill.month.toUpperCase(),
      this.cateringBill.year, this.cateringBill.dailyCateringOrders).subscribe(
        cateringBill => {
          this.cateringBillOperationsService.sendCateringBillToParent(cateringBill.id).subscribe(
            response => {
              this.nestedModalRef = this.dialogModalService.openNestedInformationModal(ACTION_COMPLETED_HEADER, `Catering bill has been saved and sent to parent.`)
              this.nestedModalRef.content.onClose.subscribe(
                onClose => {
                  this.modalRef.hide()
                  this.onClose.next(true)
                })
            },
            err => {
              this.nestedModalRef = this.dialogModalService.openNestedInformationModal(ERROR_HEADER, `Catering bill has been saved but an issue occured when trying to send the bill to parent.\n
              You may open the saved bill later and try to re-send an email.`)
              this.nestedModalRef.content.onClose.subscribe(
                onClose => {
                  this.modalRef.hide()
                  this.onClose.next(true)
                })
            })
        },
        err => {
          this.dialogModalService.openNestedInformationModal(ERROR_HEADER, err.getErrorMessage())
          this.inputDisabled = false
        })
  }

  onCancel() {
    this.modalRef.hide()
    this.onClose.next(false)
  }

}
