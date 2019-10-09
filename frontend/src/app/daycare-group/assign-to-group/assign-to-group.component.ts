import { Component, OnInit, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { DaycareGroupDataService } from '../daycare-group-data.service';
import { Subject } from 'rxjs';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { DaycareGroup } from '../daycare-group';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { ERROR_HEADER } from 'src/app/const';

@Component({
  selector: 'app-assign-to-group',
  templateUrl: './assign-to-group.component.html',
  styleUrls: ['./assign-to-group.component.css']
})
export class AssignToGroupComponent implements OnInit {

  @Input() private childId: number
  private onClose: Subject<DaycareGroup>
  private assignChildToGroupForm: FormGroup
  private daycareGroups: DaycareGroup[]

  constructor(
    private modalRef: BsModalRef,
    private dialogModalService: DialogModalService,
    private daycareGroupDataService: DaycareGroupDataService
  ) { }

  ngOnInit() {
    this.onClose = new Subject<DaycareGroup>()
    this.assignChildToGroupForm = new FormGroup({
      daycareGroup: new FormControl(null, [Validators.required])
    })
    this.retrieveDaycareGroups()
  }

  retrieveDaycareGroups() {
    this.daycareGroupDataService.retrieveDaycareGroups().subscribe(
      daycareGroups => {
        this.daycareGroups = daycareGroups
        if (daycareGroups.length > 0) {
          this.assignChildToGroupForm.patchValue({ daycareGroup: daycareGroups[0] })
        }
      })
  }

  onSubmit() {
    if (this.assignChildToGroupForm.valid) {
      this.daycareGroupDataService.addChildToDaycareGroup(
        this.assignChildToGroupForm.get('daycareGroup').value.id,
        this.childId
      ).subscribe(
        daycareGroup => {
          this.modalRef.hide()
          this.onClose.next(daycareGroup)
        },
        err => {
          this.dialogModalService.openNestedInformationModal(ERROR_HEADER, err.message)
        })
    }
  }

  onCancel() {
    this.modalRef.hide()
    this.onClose.next(null)
  }
}
