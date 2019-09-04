import { Component, OnInit, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { CateringOptionDataService } from '../catering-option-data.service';
import { CateringOption } from '../CateringOption';
import { Subject } from 'rxjs';
import { FormGroup, FormControl, Validators } from '@angular/forms';



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
    this.onClose = new Subject<boolean>()
    if (this.cateringOption.id === -1) {
      this.header = 'New catering option'
    } else {
      this.header = `Edit catering option ${this.cateringOption.id}`
    }
    let priceFormat = /^([1-9](\d+)?(\.)?(\d)?(\d)?|0|0\.(\d)?(\d)?)$/
    this.cateringOptionForm = new FormGroup({
      optionName: new FormControl('', [Validators.required, Validators.maxLength(25)]),
      dailyCost: new FormControl(0, [Validators.required, Validators.pattern(priceFormat), Validators.max(20)]),
      disabled: new FormControl(false)
    })
    this.cateringOptionForm.patchValue({
      optionName: this.cateringOption.optionName,
      dailyCost: this.cateringOption.dailyCost,
      disabled: this.cateringOption.disabled
    })
  }

  onSubmit() {
    console.log(parseFloat(this.cateringOptionForm.get('dailyCost').value))
  }

  onCancel() {
    this.bsModalRef.hide()
    this.onClose.next(false)
  }

}
