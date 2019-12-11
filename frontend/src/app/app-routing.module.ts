import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ChildPageComponent } from './child/child-page/child-page.component';
import { CateringOptionsListComponent } from './catering-option/catering-options-list/catering-options-list.component';
import { DaycareGroupPageComponent } from './daycare-group/daycare-group-page/daycare-group-page.component';
import { DaycareGroupListComponent } from './daycare-group/daycare-group-list/daycare-group-list.component';
import { UnassignedChildrenListComponent } from './child/unassigned-children-list/unassigned-children-list.component';
import { ChildrenSearchResultsComponent } from './child/children-search-results/children-search-results.component';
import { TrackAttendanceComponent } from './attendance/track-attendance/track-attendance.component';
import { ArchivedChildrenListComponent } from './child/archived-children-list/archived-children-list.component';
import { GenerateCateringBillsComponent } from './catering-bill/generate-catering-bills/generate-catering-bills.component';


const routes: Routes = [
  { path: 'child-page/:childId', component: ChildPageComponent },
  { path: 'unassigned-children-list', component: UnassignedChildrenListComponent },
  { path: 'children-search-results/:searchPhrase', component: ChildrenSearchResultsComponent },
  { path: 'children-archive', component: ArchivedChildrenListComponent },
  { path: 'catering-options-list', component: CateringOptionsListComponent },
  { path: 'daycare-group-page/:groupId', component: DaycareGroupPageComponent },
  { path: 'daycare-group-list', component: DaycareGroupListComponent },
  { path: 'track-attendance', component: TrackAttendanceComponent },
  { path: 'generate-catering-bills', component: GenerateCateringBillsComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
