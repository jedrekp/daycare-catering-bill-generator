import { Component, OnInit, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { CateringOptionDataService } from '../catering-option-data.service';
import { CateringOption } from '../CateringOption';


@Component({
  selector: 'app-catering-option-create-edit',
  templateUrl: './catering-option-create-edit.component.html',
  styleUrls: ['./catering-option-create-edit.component.css']
})
export class CateringOptionCreateEditComponent implements OnInit {

  @Input() private cateringOption: CateringOption
  private onClose: Subject<boolean>
  private header: string
  private cateringOptionForm: FormGroup

  constructor(
    private bsModalRef: BsModalRef,
    private cateringOptionDataService: CateringOptionDataService
  ) { }

  ngOnInit() {
    if (this.cateringOption.id === -1) {
      this.header = 'New catering option'
    } else {
      this.header = `Edit catering option #${this.cateringOption.id}`
    }
    this.cateringOptionForm = new FormGroup({
      optionName: new FormControl('', [Validators.required, Validators.maxLength(25)]),
      dailyCost: new FormControl(0.00, [Validators.required]),
      disabled: new FormControl(false)
    })
    this.cateringOptionForm.patchValue({
      optionName: this.cateringOption.optionName,
      dailyCost: this.cateringOption.dailyCost,
      disabled: this.cateringOption.disabled
    })
  }

  onSubmit() {
    this.bsModalRef.hide()
    this.onClose.next(true)
  }

  onCancel() {
    this.bsModalRef.hide()
    this.onClose.next(null)
  }
}
