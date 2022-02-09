import { Component, OnInit, ViewChild } from '@angular/core';
import { UserDataService } from 'src/app/service/data/user-data.service';
import { GROUP_SUPERVISOR } from 'src/const';
import { GroupSupervisorsAllProfilesComponent } from '../group-supervisors-all-profiles/group-supervisors-all-profiles.component';
import { GroupSupervisorsUnassignedProfilesComponent } from '../group-supervisors-unassigned-profiles/group-supervisors-unassigned-profiles.component';
import { User } from '../user';

@Component({
  selector: 'app-group-supervisors-list',
  templateUrl: './group-supervisors-list.component.html',
  styleUrls: ['./group-supervisors-list.component.scss']
})
export class GroupSupervisorsListComponent implements OnInit {

  @ViewChild(GroupSupervisorsAllProfilesComponent) allProfiles: GroupSupervisorsAllProfilesComponent;
  @ViewChild(GroupSupervisorsUnassignedProfilesComponent) unassignedProfiles: GroupSupervisorsUnassignedProfilesComponent;


  allSupervisors: User[]
  unassignedSupervisors: User[]

  supervisorFilter = (user: User, filter: string) => {
    const firstName = user.firstName.toLowerCase()
    const lastName = user.lastName.toLowerCase()
    const fullName = `${user.firstName} ${user.lastName}`.toLowerCase()
    const username = user.username.toLowerCase()
    return firstName.startsWith(filter) || lastName.startsWith(filter) || fullName.startsWith(filter) || username.startsWith(filter)
  }

  constructor(
    private userDataService: UserDataService
  ) { }

  ngOnInit(): void {
    this.retrieveGroupSupervisors()
  }

  retrieveGroupSupervisors() {
    this.userDataService.getAllByDaycareRole(GROUP_SUPERVISOR).subscribe(
      users => {
        this.allSupervisors = users
        this.unassignedSupervisors = users.filter(user => !user.daycareGroup)
      }
    )
  }

  applyFilters(query: string) {
    this.allProfiles.applyFilter(query)
    this.unassignedProfiles.applyFilter(query)
  }

}