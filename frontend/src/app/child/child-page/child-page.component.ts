import { Component, OnInit } from '@angular/core';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { ActivatedRoute } from '@angular/router';
import { ChildDataService } from '../child-data.service';
import { Child, AssignedDiet } from '../child';
import { PreschoolGroup } from 'src/app/preschool-group/preschool-group';
import { PreschoolGroupDataService } from 'src/app/preschool-group/preschool-group-data.service';
import { ChildCreateEditComponent } from '../child-create-edit/child-create-edit.component';
import { Diet } from 'src/app/diet/Diet';
import { DatePipe } from '@angular/common';
import { DietDataService } from 'src/app/diet/diet-data.service';

@Component({
  selector: 'app-child-page',
  templateUrl: './child-page.component.html',
  styleUrls: ['./child-page.component.css']
})
export class ChildPageComponent implements OnInit {

  private modalRef: BsModalRef

  private child: Child
  private preschoolGroups: PreschoolGroup[]
  private dietOptions: Diet[]
  private newGroup: PreschoolGroup
  private newDiet: Diet
  private effectiveDate: Date


  constructor(
    private route: ActivatedRoute,
    private modalService: BsModalService,
    private datePipe: DatePipe,
    private childDataService: ChildDataService,
    private preschoolGroupDataService: PreschoolGroupDataService,
    private dietDataService: DietDataService
  ) { }

  ngOnInit() {
    this.retrieveChild(this.route.snapshot.params['childId'])
    this.retrievePreschoolGroups()
    this.retrieveDietOptions()
    this.effectiveDate = this.setCurrentDateOrMondayIfWeekend()
  }

  retrieveChild(childId: number) {
    this.childDataService.retrieveChild(childId).subscribe(
      child => {
        this.child = child
        this.sortAssignedDietsbyEffectiveDate(this.child.assignedDiets)
      })
  }

  sortAssignedDietsbyEffectiveDate(assignedDiets: AssignedDiet[]) {
    assignedDiets.sort((val1, val2) => {
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

  retrieveDietOptions() {
    this.dietDataService.retrieveAllDiets().subscribe(
      diets => {
        this.dietOptions = diets
        this.newDiet = diets[0]
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
        this.sortAssignedDietsbyEffectiveDate(this.child.assignedDiets)
      })
  }

  assignNewDiet() {
    this.childDataService.assignNewDietToChild(this.child.id, this.newDiet.id,
      this.datePipe.transform(this.effectiveDate, 'yyyy/MM/dd')).subscribe(
        child => {
          this.child = child
          this.sortAssignedDietsbyEffectiveDate(this.child.assignedDiets)
        })
  }

  removeAssignedDiet(assignedDietId: number) {
    this.childDataService.removeAssignedDietFromChild(this.child.id, assignedDietId).subscribe(
      response => {
        this.retrieveChild(this.child.id);
      })
  }

}
