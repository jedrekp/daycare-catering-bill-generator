import { Component, OnInit, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs';

import { Child } from '../child';
import { ChildDataService } from '../child-data.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { ERROR_HEADER } from 'src/app/const';
import { ErrorHandlerService } from 'src/app/error/error-handler.service';

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
    private dialogModalService: DialogModalService,
    private childDataService: ChildDataService,
    private errorHandlerService: ErrorHandlerService
  ) { }

  ngOnInit() {
    this.onClose = new Subject<number>()
    if (this.child.id === -1) {
      this.header = 'New child'
    } else {
      this.header = `Edit child #${this.child.id}`
    }
    this.childBasicInfoForm = new FormGroup({
      firstName: new FormControl('', [Validators.required, Validators.maxLength(20)]),
      lastName: new FormControl('', [Validators.required, Validators.maxLength(20)]),
      parentEmail: new FormControl('', [Validators.required, Validators.email])
    })
    this.childBasicInfoForm.patchValue({
      firstName: this.child.firstName,
      lastName: this.child.lastName,
      parentEmail: this.child.parentEmail
    })
  }

  public onSubmit() {
    if (this.childBasicInfoForm.valid) {
      let childToSubmit = new Child(
        this.child.id,
        this.childBasicInfoForm.get('firstName').value,
        this.childBasicInfoForm.get('lastName').value,
        this.childBasicInfoForm.get('parentEmail').value,
        this.child.archived
      )
      if (this.child.id == -1) {
        this.createNewChild(childToSubmit)
      } else {
        this.editChild(childToSubmit)
      }
    }
  }

  createNewChild(childToSubmit: Child) {
    this.childDataService.createChild(childToSubmit).subscribe(
      child => {
        this.bsModalRef.hide()
        this.onClose.next(child.id)
      },
      err => {
        this.dialogModalService.openNestedInformationModal(ERROR_HEADER, this.errorHandlerService.getErrorMessage(err))
      })
  }

  editChild(childToSubmit: Child) {
    this.childDataService.editChild(this.child.id, childToSubmit).subscribe(
      child => {
        this.bsModalRef.hide()
        this.onClose.next(child.id)
      },
      err => {
        this.dialogModalService.openNestedInformationModal(ERROR_HEADER, this.errorHandlerService.getErrorMessage(err))
      })
  }

  public onCancel() {
    this.bsModalRef.hide()
    this.onClose.next(null)
  }

}
