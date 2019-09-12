import { Component, OnInit, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-information-modal',
  templateUrl: './information-modal.component.html',
  styleUrls: ['./information-modal.component.css']
})
export class InformationModalComponent implements OnInit {

  @Input() private title: string
  @Input() private message: string
  private onClose: Subject<boolean>

  constructor(
    private bsModalRef: BsModalRef
  ) { }

  ngOnInit() {
    this.onClose = new Subject<boolean>()
  }

  closeModal() {
    this.bsModalRef.hide()
    this.onClose.next(true)
  }

}
