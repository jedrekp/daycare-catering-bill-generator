import { Component, OnInit, Input } from '@angular/core';
import { DaycareGroup } from '../daycare-group';
import { Subject } from 'rxjs';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { DaycareGroupDataService } from '../daycare-group-data.service';

@Component({
  selector: 'app-daycare-group-create-edit',
  templateUrl: './daycare-group-create-edit.component.html',
  styleUrls: ['./daycare-group-create-edit.component.css']
})
export class DaycareGroupCreateEditComponent implements OnInit {


  @Input() private daycareGroup: DaycareGroup
  private onClose: Subject<number>
  private header: string
  private daycareGroupForm: FormGroup

  constructor(
    private bsModalRef: BsModalRef,
    private daycareGroupDataService: DaycareGroupDataService
  ) { }

  ngOnInit() {
    this.onClose = new Subject<number>()
    if (this.daycareGroup.id === -1) {
      this.header = 'New Daycare Group'
    } else {
      this.header = `Edit Daycare Group #${this.daycareGroup.id}`
    }
    this.daycareGroupForm = new FormGroup({
      groupName: new FormControl('', [Validators.required, Validators.maxLength(15)])
    })
    this.daycareGroupForm.patchValue({
      groupName: this.daycareGroup.groupName
    })
  }

  public onSubmit() {
    if (this.daycareGroupForm.valid) {
      let daycareGroupToSubmit = new DaycareGroup(
        this.daycareGroup.id,
        this.daycareGroupForm.get('groupName').value
      )
      if (this.daycareGroup.id === -1) {
        this.daycareGroupDataService.createDaycareGroup(daycareGroupToSubmit).subscribe(
          daycareGroup => {
            this.bsModalRef.hide()
            this.onClose.next(daycareGroup.id)
          })
      } else {
        this.daycareGroupDataService.editDaycareGroup(this.daycareGroup.id, daycareGroupToSubmit).subscribe(
          daycareGroup => {
            this.bsModalRef.hide()
            this.onClose.next(daycareGroup.id)
          })
      }
    }
  }

  public onCancel() {
    this.bsModalRef.hide()
    this.onClose.next(null)
  }

}