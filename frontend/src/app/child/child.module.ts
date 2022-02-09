import { NgModule } from '@angular/core';
import { SharedModule } from '../shared.module';
import { ChildAssignOptionModalComponent } from './child-assign-option-modal/child-assign-option-modal.component';
import { CreateChildFormComponent } from './create-child-form/create-child-form.component';
import { ChildProfilesMainComponent } from './child-profiles-main/child-profiles-main.component';
import { ChildArchivedProfilesComponent } from './child-archived-profiles/child-archived-profiles.component';
import { ChildUnassignedProfilesComponent } from './child-unassigned-profiles/child-unassigned-profiles.component';
import { ChildActiveProfilesComponent } from './child-active-profiles/child-active-profiles.component';
import { ChildProfileListComponent } from './child-profile-list/child-profile-list.component';
import { EditChildModalComponent } from './edit-child-modal/edit-child-modal.component';
import { ChildAssignedOptionsComponent } from './child-assigned-options/child-assigned-options.component';
import { ChildDaycareGroupInfoComponent } from './child-daycare-group-info/child-daycare-group-info.component';
import { ChildBasicInfoComponent } from './child-basic-info/child-basic-info.component';
import { ChildProfileComponent } from './child-profile/child-profile.component';
import { AttendanceModule } from '../attendance/attendance.module';
import { ChildAssignToGroupModalComponent } from './child-assign-to-group-modal/child-assign-to-group-modal.component';



@NgModule({
  declarations: [
    ChildProfileComponent,
    ChildBasicInfoComponent,
    ChildDaycareGroupInfoComponent,
    ChildAssignedOptionsComponent,
    EditChildModalComponent,
    ChildProfileListComponent,
    ChildActiveProfilesComponent,
    ChildUnassignedProfilesComponent,
    ChildArchivedProfilesComponent,
    ChildProfilesMainComponent,
    CreateChildFormComponent,
    ChildAssignOptionModalComponent,
    ChildAssignToGroupModalComponent
  ],
  imports: [
    SharedModule,
    AttendanceModule
  ]
})
export class ChildModule { }
