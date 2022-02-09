import { NgModule } from '@angular/core';
import { GroupSupervisorsMainComponent } from './group-supervisors-main/group-supervisors-main.component';
import { GroupSupervisorsListComponent } from './group-supervisors-list/group-supervisors-list.component';
import { CreateGroupSupervisorFormComponent } from './create-group-supervisor-form/create-group-supervisor-form.component';
import { SharedModule } from '../shared.module';
import { GroupSupervisorsAllProfilesComponent } from './group-supervisors-all-profiles/group-supervisors-all-profiles.component';
import { GroupSupervisorsUnassignedProfilesComponent } from './group-supervisors-unassigned-profiles/group-supervisors-unassigned-profiles.component';
import { AssignGroupToSupervisorModalComponent } from './assign-group-to-supervisor-modal/assign-group-to-supervisor-modal.component';
import { GroupSupervisorPageComponent } from './group-supervisor-page/group-supervisor-page.component';
import { UserPageComponent } from './user-page/user-page.component';
import { ChangePasswordModalComponent } from './change-password-modal/change-password-modal.component';
import { LoginComponent } from '../login/login.component';



@NgModule({
  declarations: [
    UserPageComponent,
    ChangePasswordModalComponent,
    GroupSupervisorsMainComponent,
    GroupSupervisorsListComponent,
    GroupSupervisorsAllProfilesComponent,
    GroupSupervisorsUnassignedProfilesComponent,
    GroupSupervisorPageComponent,
    CreateGroupSupervisorFormComponent,
    AssignGroupToSupervisorModalComponent,
    LoginComponent
  ],
  imports: [
    SharedModule
  ]
})
export class UserModule { }
