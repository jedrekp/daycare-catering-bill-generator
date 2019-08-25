import { Component, OnInit, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs';

import { Child } from '../child';
import { ChildDataService } from '../child-data.service';

@Component({
  selector: 'app-child-create-edit',
  templateUrl: './child-create-edit.component.html',
  styleUrls: ['./child-create-edit.component.css']
})
export class ChildCreateEditComponent implements OnInit {

  @Input() private child: Child
  private onClose: Subject<number>
  private header: string

  constructor(
    private bsModalRef: BsModalRef,
    private childDataService: ChildDataService
  ) { }

  ngOnInit() {
    this.onClose = new Subject<number>()
    if (this.child.id == -1) {
      this.header = 'New child'
    } else {
      this.header = `Edit child #${this.child.id}`
    }
  }

  public onSave() {
    if (this.child.id == -1) {
      this.childDataService.createChild(this.child).subscribe(
        child => {
          this.bsModalRef.hide()
          this.onClose.next(child.id)
        })
    } else {
      this.childDataService.editChild(this.child.id, this.child).subscribe(
        child => {
          this.bsModalRef.hide()
          this.onClose.next(this.child.id)
        })
    }
  }

  public onCancel() {
    this.bsModalRef.hide()
    this.onClose.next(null)
  }

}
