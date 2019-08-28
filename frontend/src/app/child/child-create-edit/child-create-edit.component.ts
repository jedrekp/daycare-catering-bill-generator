import { Component, OnInit, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs';

import { Child } from '../child';
import { ChildDataService } from '../child-data.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-child-create-edit',
  templateUrl: './child-create-edit.component.html',
  styleUrls: ['./child-create-edit.component.css']
})
export class ChildCreateEditComponent implements OnInit {

  @Input() private child: Child
  private onClose: Subject<number>
  private header: string
  private childBasicInfoForm: FormGroup

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
    this.childBasicInfoForm = new FormGroup({
      firstName: new FormControl(this.child.firstName, [Validators.required, Validators.maxLength(25)]),
      lastName: new FormControl(this.child.lastName, [Validators.required, Validators.maxLength(25)])
    })
  }

  public onSubmit() {
    if (this.childBasicInfoForm.valid) {
      let childToSubmit = new Child(
        this.child.id, this.childBasicInfoForm.get('firstName').value,
        this.childBasicInfoForm.get('lastName').value
      )
      if (this.child.id == -1) {
        this.childDataService.createChild(childToSubmit).subscribe(
          child => {
            this.bsModalRef.hide()
            this.onClose.next(child.id)
          })
      } else {
        this.childDataService.editChild(this.child.id, childToSubmit).subscribe(
          child => {
            this.bsModalRef.hide()
            this.onClose.next(this.child.id)
          })
      }
    }
  }

  public onCancel() {
    this.bsModalRef.hide()
    this.onClose.next(null)
  }

}
