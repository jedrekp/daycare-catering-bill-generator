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
import { LoginComponent } from './authentication/login/login.component';
import { WelcomePageComponent } from './welcome-page/welcome-page.component';
import { RouteGuardService } from './authentication/route-guard.service';
import { ErrorComponent } from './error/error.component';
import { UserPageComponent } from './user/user-page/user-page.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: '', component: WelcomePageComponent, canActivate: [RouteGuardService] },
  { path: 'welcome-page', component: WelcomePageComponent, canActivate: [RouteGuardService] },
  { path: 'child-page/:childId', component: ChildPageComponent, canActivate: [RouteGuardService] },
  { path: 'unassigned-children-list', component: UnassignedChildrenListComponent, canActivate: [RouteGuardService] },
  { path: 'children-search-results/:searchPhrase', component: ChildrenSearchResultsComponent, canActivate: [RouteGuardService] },
  { path: 'children-archive', component: ArchivedChildrenListComponent, canActivate: [RouteGuardService] },
  { path: 'catering-options-list', component: CateringOptionsListComponent, canActivate: [RouteGuardService] },
  { path: 'daycare-group-page/:groupId', component: DaycareGroupPageComponent, canActivate: [RouteGuardService] },
  { path: 'daycare-group-list', component: DaycareGroupListComponent, canActivate: [RouteGuardService] },
  { path: 'track-attendance', component: TrackAttendanceComponent, canActivate: [RouteGuardService] },
  { path: 'generate-catering-bills', component: GenerateCateringBillsComponent, canActivate: [RouteGuardService], data: { authorizedUserRoles: ['HEADMASTER'] } },
  { path: 'user-page/:username', component: UserPageComponent, canActivate: [RouteGuardService] },
  { path: 'error', component: ErrorComponent },

  { path: '**', component: ErrorComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
