import { NgModule } from '@angular/core';
import { DaycareGroupPageComponent } from './daycare-group-page/daycare-group-page.component';
import { DaycareGroupBasicInfoComponent } from './daycare-group-basic-info/daycare-group-basic-info.component';
import { DaycareGroupsListComponent } from './daycare-groups-list/daycare-groups-list.component';
import { SharedModule } from '../shared.module';
import { DaycareGroupsMainComponent } from './daycare-groups-main/daycare-groups-main.component';
import { CreateDaycareGroupFormComponent } from './create-daycare-group-form/create-daycare-group-form.component';
import { DaycareGroupChildrenListComponent } from './daycare-group-children-list/daycare-group-children-list.component';
import { EditDaycareGroupModalComponent } from './edit-daycare-group-modal/edit-daycare-group-modal.component';
import { DaycareGroupAssignChildModalComponent } from './daycare-group-assign-child-modal/daycare-group-assign-child-modal.component';



@NgModule({
  declarations: [
    DaycareGroupPageComponent,
    DaycareGroupBasicInfoComponent,
    DaycareGroupChildrenListComponent,
    DaycareGroupsMainComponent,
    DaycareGroupsListComponent,
    CreateDaycareGroupFormComponent,
    EditDaycareGroupModalComponent,
    DaycareGroupAssignChildModalComponent
  ],
  imports: [
    SharedModule
  ]
})
export class DaycareGroupModule { }
