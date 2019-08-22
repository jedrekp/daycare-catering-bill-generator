import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ChildDataService } from '../child-data.service';
import { Child } from '../child';
import { PreschoolGroup } from 'src/app/preschool-group/preschool-group';
import { PreschoolGroupDataService } from 'src/app/preschool-group/preschool-group-data.service';

@Component({
  selector: 'app-child-page',
  templateUrl: './child-page.component.html',
  styleUrls: ['./child-page.component.css']
})
export class ChildPageComponent implements OnInit {

  private childId: number
  private child: Child
  private preschoolGroups: PreschoolGroup[]
  private newGroupId: number

  constructor(
    private route: ActivatedRoute,
    private childDataService: ChildDataService,
    private preschoolGroupDataService: PreschoolGroupDataService
  ) { }

  ngOnInit() {
    this.childId = this.route.snapshot.params['childId']
    this.childDataService.retrieveChild(this.childId).subscribe(
      child => {
        this.child = child
      })
    this.preschoolGroupDataService.retrieveGroups().subscribe(
      preschoolGroups => {
        this.preschoolGroups = preschoolGroups
      })
  }

  assignToNewGroup() {
    this.childDataService.assignToPreschoolGroup(this.child.id, this.newGroupId).subscribe(
      child => {
        this.child = child
      })
  }

}
