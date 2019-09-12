import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ChildPageComponent } from './child/child-page/child-page.component';
import { CateringOptionsListComponent } from './catering-option/catering-options-list/catering-options-list.component';
import { DaycareGroupPageComponent } from './daycare-group/daycare-group-page/daycare-group-page.component';
import { DaycareGroupListComponent } from './daycare-group/daycare-group-list/daycare-group-list.component';


const routes: Routes = [
  { path: 'child-page/:childId', component: ChildPageComponent },
  { path: 'catering-options-list', component: CateringOptionsListComponent },
  { path: 'daycare-group-page/:groupId', component: DaycareGroupPageComponent },
  { path: 'daycare-group-list', component: DaycareGroupListComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
