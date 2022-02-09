import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { timer } from 'rxjs';
import { finalize, switchMap } from 'rxjs/operators';
import { CateringBillOperationsService } from 'src/app/service/data/catering-bill-operations.service';
import { ChildDataService } from 'src/app/service/data/child-data.service';
import { UserDialogComponent } from 'src/app/user-dialog/user-dialog.component';

@Component({
  selector: 'app-sending-bill',
  templateUrl: './sending-bill.component.html',
  styleUrls: ['./sending-bill.component.scss']
})
export class SendingBillComponent implements OnInit {

  inProgress: boolean
  title: string
  userMessage: string

  constructor(
    public dialogRef: MatDialogRef<UserDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private childDataService: ChildDataService,
    private cateringBillOperationsService: CateringBillOperationsService
  ) { }

  ngOnInit(): void {
    this.title = 'Sending Email...'
    this.inProgress = true
    if (this.data.newBill)
      this.saveBillThenSendEmail()
    else
      this.sendEmailToParent(this.data.cateringBill.billId)
  }

  saveBillThenSendEmail() {
    this.childDataService.saveCateringBill(this.data.cateringBill).subscribe(
      bill => {
        this.sendEmailToParent(bill.billId)
      }
    )
  }

  sendEmailToParent(billId: number) {
    timer(2000).pipe(
      switchMap(
        () => this.cateringBillOperationsService.sendCateringBillToParent(billId)
      ),
      finalize(
        () => this.inProgress = false
      )
    ).subscribe(
      () => this.showUserMessage(true),
      err => this.showUserMessage(false)
    )
  }

  showUserMessage(success: boolean) {
    if (success) {
      this.title = 'Email Sent to Parent'
      if (this.data.newBill)
        this.userMessage = '<p>Catering bill has been saved and sent to parent.</p>'
      else
        this.userMessage = '<p>Catering bill has been succesfully sent to parent.</p>'
    } else {
      this.title = 'Email Not Delivered'
      if (this.data.newBill)
        this.userMessage = `<p>Catering bill has been saved but an issue occured when trying to send the bill to parent.</p>
      <p>You may view the saved bill later and try to re-send an email.</p>`
      else
        this.userMessage = `<p>An issue occured when trying to send catering bill to parent.</p>
         <p>Please contact server administrator or try again later.</p>`
    }
  }

}
