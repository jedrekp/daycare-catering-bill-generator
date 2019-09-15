import { Component, OnInit, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { CateringOptionDataService } from '../catering-option-data.service';
import { CateringOption } from '../CateringOption';
import { Subject } from 'rxjs';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';



@Component({
  selector: 'app-catering-option-create-edit',
  templateUrl: './catering-option-create-edit.component.html',
  styleUrls: ['./catering-option-create-edit.component.css']
})
export class CateringOptionCreateEditComponent implements OnInit {

  @Input() private cateringOption: CateringOption
  private onClose: Subject<CateringOption>
  private header: string
  private cateringOptionForm: FormGroup

  constructor(
    private modalRef: BsModalRef,
    private dialogModalService: DialogModalService,
    private cateringOptionDataService: CateringOptionDataService
  ) { }

  ngOnInit() {
    this.onClose = new Subject<CateringOption>()
    if (this.cateringOption.id === -1) {
      this.header = 'New catering option'
    } else {
      this.header = `Edit catering option #${this.cateringOption.id}`
    }
    const priceFormat = /^\d+(\.)?(\d{1,2})?$/
    this.cateringOptionForm = new FormGroup({
      optionName: new FormControl('', [Validators.required, Validators.maxLength(25)]),
      dailyCost: new FormControl(0, [Validators.required, Validators.pattern(priceFormat), Validators.max(30)]),
      disabled: new FormControl(false)
    })
    this.cateringOptionForm.patchValue({
      optionName: this.cateringOption.optionName,
      dailyCost: this.cateringOption.dailyCost,
      disabled: this.cateringOption.disabled
    })
  }

  onSubmit() {
    if (this.cateringOptionForm.valid) {
      let cateringOptionToSubmit = new CateringOption(
        this.cateringOption.id,
        this.cateringOptionForm.get('optionName').value,
        this.cateringOptionForm.get('dailyCost').value,
        this.cateringOptionForm.get('disabled').value
      )
      if (this.cateringOption.id === -1) {
        this.cateringOptionDataService.createCateringOption(cateringOptionToSubmit).subscribe(
          cateringOption => {
            this.modalRef.hide()
            this.onClose.next(cateringOption)
          },
          err => {
            this.dialogModalService.openNestedInformationModal('Cannot create option', err.message)
          })
      } else {
        this.cateringOptionDataService.editCateringOption(
          cateringOptionToSubmit, this.cateringOption.id).subscribe(
            cateringOption => {
              this.modalRef.hide()
              this.onClose.next(cateringOption)
            },
            err => {
              this.dialogModalService.openNestedInformationModal('Cannot edit option', err.message)
            })
      }
    }
  }

  onCancel() {
    this.modalRef.hide()
    this.onClose.next(null)
  }

}
