import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HEADMASTER } from 'src/const';
import { GroupAttendanceMainComponent } from './attendance/track-group-attendance/group-attendance-main.component';
import { CateringBillsMainComponent } from './catering-bill/catering-bills-main/catering-bills-main.component';
import { CateringOptionsMainComponent } from './catering-option/catering-options-main/catering-options-main.component';
import { ChildProfileComponent } from './child/child-profile/child-profile.component';
import { ChildProfilesMainComponent } from './child/child-profiles-main/child-profiles-main.component';
import { ErrorPageComponent } from './common/error/error-page/error-page.component';
import { DaycareGroupPageComponent } from './daycare-group/daycare-group-page/daycare-group-page.component';
import { DaycareGroupsMainComponent } from './daycare-group/daycare-groups-main/daycare-groups-main.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { AuthGuardService } from './service/authentication/auth-guard.service';
import { GroupSupervisorPageComponent } from './user/group-supervisor-page/group-supervisor-page.component';
import { GroupSupervisorsMainComponent } from './user/group-supervisors-main/group-supervisors-main.component';
import { UserPageComponent } from './user/user-page/user-page.component';

const routes: Routes = [
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  },
  {
    path: 'error',
    component: ErrorPageComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'user-profile',
    component: UserPageComponent
  },
  {
    path: 'child-profiles',
    component: ChildProfilesMainComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: 'child-profiles/:id',
    component: ChildProfileComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: 'daycare-groups',
    component: DaycareGroupsMainComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: 'daycare-groups/:id',
    component: DaycareGroupPageComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: 'attendance',
    component: GroupAttendanceMainComponent
  },
  {
    path: 'group-supervisors',
    component: GroupSupervisorsMainComponent,
    canActivate: [AuthGuardService],
    data: { authorizedUserRoles: [HEADMASTER] }
  },
  {
    path: 'group-supervisors/:username',
    component: GroupSupervisorPageComponent,
    canActivate: [AuthGuardService],
    data: { authorizedUserRoles: [HEADMASTER] }
  },
  {
    path: 'catering-options',
    component: CateringOptionsMainComponent,
    canActivate: [AuthGuardService],
    data: { authorizedUserRoles: [HEADMASTER] }
  },
  {
    path: 'catering-bills',
    component: CateringBillsMainComponent,
    canActivate: [AuthGuardService],
    data: { authorizedUserRoles: [HEADMASTER] }
  },
  {
    path: '**',
    redirectTo: 'home'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
