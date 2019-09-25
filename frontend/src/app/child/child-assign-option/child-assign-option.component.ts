import { Component, OnInit, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs';
import { ChildDataService } from '../child-data.service';
import { CateringOption } from 'src/app/catering-option/CateringOption';
import { CateringOptionDataService } from 'src/app/catering-option/catering-option-data.service';
import { DatePipe } from '@angular/common';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { ERROR_HEADER } from 'src/app/const';

@Component({
  selector: 'app-child-assign-option',
  templateUrl: './child-assign-option.component.html',
  styleUrls: ['./child-assign-option.component.css']
})

export class ChildAssignOptionComponent implements OnInit {

  @Input() private childId: number
  private onClose: Subject<CateringOption>
  private assignCateringOptionForm: FormGroup
  private cateringOptions: CateringOption[]
  private minDate: Date

  constructor(
    private bsModalRef: BsModalRef,
    private datePipe: DatePipe,
    private dialogModalService: DialogModalService,
    private childDataService: ChildDataService,
    private cateringOptionDataService: CateringOptionDataService
  ) { }

  ngOnInit() {
    this.onClose = new Subject<CateringOption>()
    this.assignCateringOptionForm = new FormGroup({
      effectiveDate: new FormControl(null, [Validators.required]),
      cateringOption: new FormControl(null, [Validators.required])
    })
    this.assignCateringOptionForm.patchValue({ effectiveDate: this.setCurrentDateOrMondayIfWeekend() })
    this.retrieveCateringOptions()
    this.minDate = new Date(2019, 0, 1)
  }

  retrieveCateringOptions() {
    this.cateringOptionDataService.retriveCateringOptionsByDisabled(false).subscribe(
      cateringOptions => {
        this.cateringOptions = cateringOptions
        if (cateringOptions.length > 0) {
          this.assignCateringOptionForm.patchValue({ cateringOption: cateringOptions[0] })
        }
      })
  }

  setCurrentDateOrMondayIfWeekend(): Date {
    let date = new Date();
    if (date.getDay() === 0) {
      date.setDate(date.getDate() + 1)
    } else if (date.getDay() === 6) {
      date.setDate(date.getDate() + 2)
    }
    return date
  }

  onSubmit() {
    if (this.assignCateringOptionForm.valid) {
      this.childDataService.assignNewOptionToChild(
        this.childId,
        this.assignCateringOptionForm.get('cateringOption').value.id,
        this.datePipe.transform(this.assignCateringOptionForm.get('effectiveDate').value, 'yyyy-MM-dd'))
        .subscribe(
          response => {
            this.bsModalRef.hide()
            this.onClose.next(this.assignCateringOptionForm.get('cateringOption').value)
          },
          err => {
            this.dialogModalService.openNestedInformationModal(ERROR_HEADER, err.message)
          })
    }
  }

  onCancel() {
    this.bsModalRef.hide()
    this.onClose.next(null)
  }
}
