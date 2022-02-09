import { Component, OnInit, ViewChild } from '@angular/core';
import { ChildDataService } from 'src/app/service/data/child-data.service';
import { JwtAuthService } from 'src/app/service/authentication/jwt-auth.service';
import { Child } from '../child';
import { ChildActiveProfilesComponent } from '../child-active-profiles/child-active-profiles.component';
import { ChildArchivedProfilesComponent } from '../child-archived-profiles/child-archived-profiles.component';
import { ChildUnassignedProfilesComponent } from '../child-unassigned-profiles/child-unassigned-profiles.component';

@Component({
  selector: 'app-child-profile-list',
  templateUrl: './child-profile-list.component.html',
  styleUrls: ['./child-profile-list.component.scss']
})
export class ChildProfileListComponent implements OnInit {

  @ViewChild(ChildActiveProfilesComponent) active : ChildActiveProfilesComponent;
  @ViewChild(ChildUnassignedProfilesComponent) unassigned : ChildUnassignedProfilesComponent;
  @ViewChild(ChildArchivedProfilesComponent) archived : ChildArchivedProfilesComponent;

  activeChildren: Child[]
  unassignedChildren: Child[]
  archivedChildren: Child[]

  childProfileFilter = (child: Child, filter: string) => {
    const firstName = child.firstName.toLowerCase()
    const lastName = child.lastName.toLowerCase()
    const fullName = `${child.firstName} ${child.lastName}`.toLowerCase()
    return firstName.startsWith(filter) || lastName.startsWith(filter) || fullName.startsWith(filter)
  }

  constructor(
    public authService: JwtAuthService,
    private childDataService: ChildDataService
  ) { }

  ngOnInit(): void {
    this.retrieveChildren()
  }

  retrieveChildren() {
    this.childDataService.getAll().subscribe(
      children => {
        this.activeChildren = children.filter(child => !child.archived)
        this.unassignedChildren = children.filter(child => !child.daycareGroup && !child.archived)
        this.archivedChildren = children.filter(child => child.archived)
      })
  }

  applyFilters(query: string){
    this.active.applyFilter(query)
    this.unassigned.applyFilter(query)
    this.archived.applyFilter(query)
  }

}
