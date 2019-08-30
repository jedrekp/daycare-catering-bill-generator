import { Component, OnInit } from '@angular/core';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { ActivatedRoute } from '@angular/router';
import { ChildDataService } from '../child-data.service';
import { Child, AssignedOption } from '../child';
import { ChildCreateEditComponent } from '../child-create-edit/child-create-edit.component';
import { ChildAssignOptionComponent } from '../child-assign-option/child-assign-option.component';
import { DaycareGroup } from 'src/app/daycare-group/daycare-group';
import { DaycareGroupDataService } from 'src/app/daycare-group/daycare-group-data.service';

@Component({
  selector: 'app-child-page',
  templateUrl: './child-page.component.html',
  styleUrls: ['./child-page.component.css']
})
export class ChildPageComponent implements OnInit {

  private modalRef: BsModalRef

  private child: Child
  private daycareGroups: DaycareGroup[]
  private newGroup: DaycareGroup

  constructor(
    private route: ActivatedRoute,
    private modalService: BsModalService,
    private childDataService: ChildDataService,
    private daycareGroupDataService: DaycareGroupDataService,
  ) { }

  ngOnInit() {
    this.retrieveChild(this.route.snapshot.params['childId'])
    this.retrieveDaycareGroups()
  }

  retrieveChild(childId: number) {
    this.childDataService.retrieveChild(childId).subscribe(
      child => {
        this.child = child
        this.sortAssignedOptionsbyEffectiveDate(this.child.assignedOptions)
      })
  }

  sortAssignedOptionsbyEffectiveDate(assignedOptions: AssignedOption[]) {
    assignedOptions.sort((val1, val2) => {
      return <any>new Date(val2.effectiveDate) - <any>new Date(val1.effectiveDate)
    })
  }

  retrieveDaycareGroups() {
    this.daycareGroupDataService.retrieveGroups().subscribe(
      daycareGroups => {
        this.daycareGroups = daycareGroups
        this.newGroup = daycareGroups[0]
      })
  }

  openEditChildModal() {
    let initialState = { child: new Child(this.child.id, this.child.firstName, this.child.lastName) };
    this.modalRef = this.modalService.show(ChildCreateEditComponent,
      { class: 'modal-top-20 modal-sm', initialState, ignoreBackdropClick: true })
    this.modalRef.content.onClose.subscribe(
      childId => {
        if (childId) {
          this.retrieveChild(childId)
        }
      })
  }

  assignToNewGroup() {
    this.daycareGroupDataService.addSingleChildToDaycareGroup(this.newGroup.id, this.child.id).subscribe(
      daycareGroup => {
        console.log(this.child.id);
        this.retrieveChild(this.child.id)
      })
  }

  openAssignNewOptionModal() {
    let initialState = { childId: this.child.id }
    this.modalRef = this.modalService.show(ChildAssignOptionComponent,
      { class: 'modal-top-20 modal-sm', initialState, ignoreBackdropClick: true })
    this.modalRef.content.onClose.subscribe(
      childId => {
        if (childId) {
          this.retrieveChild(childId)
        }
      })
  }

  removeAssignedOption(assignedOptionId: number) {
    this.childDataService.removeAssignedOptionFromChild(this.child.id, assignedOptionId).subscribe(
      response => {
        this.retrieveChild(this.child.id);
      })
  }

}
