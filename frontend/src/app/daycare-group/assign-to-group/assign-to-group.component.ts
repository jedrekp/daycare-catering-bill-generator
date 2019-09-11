import { Component, OnInit, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { DaycareGroupDataService } from '../daycare-group-data.service';
import { Subject } from 'rxjs';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { DaycareGroup } from '../daycare-group';

@Component({
  selector: 'app-assign-to-group',
  templateUrl: './assign-to-group.component.html',
  styleUrls: ['./assign-to-group.component.css']
})
export class AssignToGroupComponent implements OnInit {

  @Input() private childId: number
  private onClose: Subject<boolean>
  private assignChildToGroupForm: FormGroup
  private daycareGroups: DaycareGroup[]

  constructor(
    private bsModalRef: BsModalRef,
    private daycareGroupDataService: DaycareGroupDataService
  ) { }

  ngOnInit() {
    this.onClose = new Subject<boolean>()
    this.assignChildToGroupForm = new FormGroup({
      daycareGroup: new FormControl(null, [Validators.required])
    })
    this.retrieveDaycareGroups()
  }

  retrieveDaycareGroups() {
    this.daycareGroupDataService.retrieveDaycareGroups().subscribe(
      daycareGroups => {
        this.daycareGroups = daycareGroups
        this.assignChildToGroupForm.patchValue({ daycareGroup: daycareGroups[0] })
      })
  }

  onSubmit() {
    this.daycareGroupDataService.addChildToDaycareGroup(
      this.assignChildToGroupForm.get('daycareGroup').value.id,
      this.childId
    ).subscribe(
      response => {
        this.bsModalRef.hide()
        this.onClose.next(true)
      })
  }

  onCancel() {
    this.bsModalRef.hide()
    this.onClose.next(false)
  }
}
