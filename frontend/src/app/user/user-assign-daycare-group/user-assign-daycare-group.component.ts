import { Component, OnInit, Input } from '@angular/core';
import { Subject } from 'rxjs';
import { DaycareGroup } from 'src/app/daycare-group/daycare-group';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { UserDataService } from '../user-data.service';
import { ErrorHandlerService } from 'src/app/error/error-handler.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { DaycareGroupDataService } from 'src/app/daycare-group/daycare-group-data.service';
import { ERROR_HEADER } from 'src/app/const';
import { User } from '../user';

@Component({
  selector: 'app-user-assign-daycare-group',
  templateUrl: './user-assign-daycare-group.component.html',
  styleUrls: ['./user-assign-daycare-group.component.css']
})
export class UserAssignDaycareGroupComponent implements OnInit {

  @Input() private userId: number
  private onClose: Subject<User>
  private assignDaycareGroupToUserForm: FormGroup
  private daycareGroups: DaycareGroup[]

  constructor(
    private modalRef: BsModalRef,
    private dialogModalService: DialogModalService,
    private daycareGroupDataService: DaycareGroupDataService,
    private userDataService: UserDataService,
    private errorHandlerService: ErrorHandlerService
  ) { }

  ngOnInit() {
    this.onClose = new Subject<User>()
    this.assignDaycareGroupToUserForm = new FormGroup({
      daycareGroup: new FormControl(null, [Validators.required])
    })
    this.retrieveDaycareGroupsWithNoSupervisors()
  }

  retrieveDaycareGroupsWithNoSupervisors() {
    this.daycareGroupDataService.retrieveDaycareGroupsByGroupSupervisorId(0).subscribe(
      daycareGroups => {
        this.daycareGroups = daycareGroups
        this.assignDaycareGroupToUserForm.patchValue({ daycareGroup: daycareGroups[0] })
      },
      err => {
        this.errorHandlerService.redirectToErrorPage(err)
      })
  }

  onSubmit() {
    if (this.assignDaycareGroupToUserForm.valid) {
      this.userDataService.assignDaycareGroupToUser(this.userId, this.assignDaycareGroupToUserForm.get('daycareGroup').value.id).subscribe(
        user => {
          this.modalRef.hide()
          this.onClose.next(user)
        },
        err => {
          this.dialogModalService.openNestedInformationModal(ERROR_HEADER, this.errorHandlerService.getErrorMessage(err))
        })
    }
  }

  onCancel() {
    this.modalRef.hide()
    this.onClose.next(null)
  }

}