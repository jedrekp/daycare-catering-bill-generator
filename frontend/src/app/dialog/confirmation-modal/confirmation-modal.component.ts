import { Component, OnInit, Input } from '@angular/core';
import { Subject } from 'rxjs';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-confirmation-modal',
  templateUrl: './confirmation-modal.component.html',
  styleUrls: ['./confirmation-modal.component.css']
})
export class ConfirmationModalComponent implements OnInit {

  @Input() private title: string
  @Input() private message: string
  private onClose: Subject<boolean>

  constructor(
    private bsModalRef: BsModalRef
  ) { }

  ngOnInit() {
    this.onClose = new Subject<boolean>()
  }

  onOK() {
    this.bsModalRef.hide()
    this.onClose.next(true)
  }

  onCancel() {
    this.bsModalRef.hide()
    this.onClose.next(false)
  }

}
