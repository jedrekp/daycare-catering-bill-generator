import { Component, OnInit } from '@angular/core';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { ActivatedRoute } from '@angular/router';
import { ChildDataService } from '../child-data.service';
import { Child, AssignedOption } from '../child';
import { PreschoolGroup } from 'src/app/preschool-group/preschool-group';
import { PreschoolGroupDataService } from 'src/app/preschool-group/preschool-group-data.service';
import { ChildCreateEditComponent } from '../child-create-edit/child-create-edit.component';
import { CateringOption } from 'src/app/catering-option/CateringOption';
import { DatePipe } from '@angular/common';
import { CateringOptionDataService } from 'src/app/catering-option/catering-option-data.service';

@Component({
  selector: 'app-child-page',
  templateUrl: './child-page.component.html',
  styleUrls: ['./child-page.component.css']
})
export class ChildPageComponent implements OnInit {

  private modalRef: BsModalRef

  private child: Child
  private preschoolGroups: PreschoolGroup[]
  private cateringOptions: CateringOption[]
  private newGroup: PreschoolGroup
  private newOption: CateringOption
  private effectiveDate: Date


  constructor(
    private route: ActivatedRoute,
    private modalService: BsModalService,
    private datePipe: DatePipe,
    private childDataService: ChildDataService,
    private preschoolGroupDataService: PreschoolGroupDataService,
    private cateringOptionDataService: CateringOptionDataService
  ) { }

  ngOnInit() {
    this.retrieveChild(this.route.snapshot.params['childId'])
    this.retrievePreschoolGroups()
    this.retrieveCateringOptions()
    this.effectiveDate = this.setCurrentDateOrMondayIfWeekend()
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
      return <any>new Date(val2.effectiveDate) - <any>new
        Date(val1.effectiveDate)
    })
  }

  retrievePreschoolGroups() {
    this.preschoolGroupDataService.retrieveGroups().subscribe(
      preschoolGroups => {
        this.preschoolGroups = preschoolGroups
        this.newGroup = preschoolGroups[0]
      })
  }

  retrieveCateringOptions() {
    this.cateringOptionDataService.retrieveAllCateringOptions().subscribe(
      cateringOptions => {
        this.cateringOptions = cateringOptions
        this.newOption = cateringOptions[0]
      })
  }

  setCurrentDateOrMondayIfWeekend(): Date {
    let date = new Date();
    if (date.getDay() == 0) {
      date.setDate(date.getDate() + 1)
    } else if (date.getDay() == 6) {
      date.setDate(date.getDate() + 2)
    }
    return date
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
    this.childDataService.assignToPreschoolGroup(this.child.id, this.newGroup.id).subscribe(
      child => {
        this.child = child
        this.sortAssignedOptionsbyEffectiveDate(this.child.assignedOptions)
      })
  }

  assignNewOption() {
    this.childDataService.assignNewOptionToChild(this.child.id, this.newOption.id,
      this.datePipe.transform(this.effectiveDate, 'yyyy/MM/dd')).subscribe(
        child => {
          this.child = child
          this.sortAssignedOptionsbyEffectiveDate(this.child.assignedOptions)
        })
  }

  removeAssignedOption(assignedOptionId: number) {
    this.childDataService.removeAssignedOptionFromChild(this.child.id, assignedOptionId).subscribe(
      response => {
        this.retrieveChild(this.child.id);
      })
  }

}
