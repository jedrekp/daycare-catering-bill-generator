import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ModalModule } from 'ngx-bootstrap/modal';
import { AlertModule } from 'ngx-bootstrap/alert';
import { AppRoutingModule } from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { DatePipe } from '@angular/common';

import { AppComponent } from './app.component';
import { ChildCreateEditComponent } from './child/child-create-edit/child-create-edit.component';
import { MenuBarComponent } from './menu-bar/menu-bar.component';
import { ChildPageComponent } from './child/child-page/child-page.component';
import { ChildAssignOptionComponent } from './child/child-assign-option/child-assign-option.component';
import { AssignToGroupComponent } from './daycare-group/assign-to-group/assign-to-group.component';
import { CateringOptionsListComponent } from './catering-option/catering-options-list/catering-options-list.component';
import { CateringOptionCreateEditComponent } from './catering-option/catering-option-create-edit/catering-option-create-edit.component';
import { DaycareGroupCreateEditComponent } from './daycare-group/daycare-group-create-edit/daycare-group-create-edit.component';
import { DaycareGroupListComponent } from './daycare-group/daycare-group-list/daycare-group-list.component';
import { DaycareGroupPageComponent } from './daycare-group/daycare-group-page/daycare-group-page.component';
import { InformationModalComponent } from './dialog/information-modal/information-modal.component';
import { ConfirmationModalComponent } from './dialog/confirmation-modal/confirmation-modal.component';
import { UnassignedChildrenListComponent } from './child/unassigned-children-list/unassigned-children-list.component';
import { ChildrenSearchResultsComponent } from './child/children-search-results/children-search-results.component';
import { TrackAttendanceComponent } from './attendance/track-attendance/track-attendance.component';
import { ArchivedChildrenListComponent } from './child/archived-children-list/archived-children-list.component';
import { GenerateCateringBillsComponent } from './catering-bill/generate-catering-bills/generate-catering-bills.component';
import { BillPreviewComponent } from './catering-bill/bill-preview/bill-preview.component';
import { BillDisplayComponent } from './catering-bill/bill-display/bill-display.component';
import { LoginComponent } from './authentication/login/login.component';
import { WelcomePageComponent } from './user/welcome-page/welcome-page.component';
import { ErrorComponent } from './error/error.component';
import { HttpInterceptorService } from './authentication/http-interceptor.service';
import { SingleChildAttendanceComponent } from './attendance/single-child-attendance/single-child-attendance.component';
import { ChildAssignedOptionsListComponent } from './child/child-assigned-options-list/child-assigned-options-list.component';
import { ChildDaycareGroupInfoComponent } from './child/child-daycare-group-info/child-daycare-group-info.component';
import { ChildBasicInfoComponent } from './child/child-basic-info/child-basic-info.component';
import { DaycareGroupBasicInfoComponent } from './daycare-group/daycare-group-basic-info/daycare-group-basic-info.component';
import { DaycareGroupChildrenListComponent } from './daycare-group/daycare-group-children-list/daycare-group-children-list.component';
import { UserCreateAccountComponent } from './user/user-create-account/user-create-account.component';
import { UserPageComponent } from './user/user-page/user-page.component';
import { UserAssignDaycareGroupComponent } from './user/user-assign-daycare-group/user-assign-daycare-group.component';
import { GroupSupervisorsListComponent } from './user/group-supervisors-list/group-supervisors-list.component';
import { UserChangePasswordComponent } from './user/user-change-password/user-change-password.component';

@NgModule({
  declarations: [
    AppComponent,
    ChildCreateEditComponent,
    MenuBarComponent,
    ChildPageComponent,
    ChildAssignOptionComponent,
    AssignToGroupComponent,
    CateringOptionsListComponent,
    CateringOptionCreateEditComponent,
    DaycareGroupCreateEditComponent,
    DaycareGroupListComponent,
    DaycareGroupPageComponent,
    InformationModalComponent,
    ConfirmationModalComponent,
    UnassignedChildrenListComponent,
    ChildrenSearchResultsComponent,
    TrackAttendanceComponent,
    ArchivedChildrenListComponent,
    GenerateCateringBillsComponent,
    BillPreviewComponent,
    BillDisplayComponent,
    LoginComponent,
    WelcomePageComponent,
    ErrorComponent,
    SingleChildAttendanceComponent,
    ChildAssignedOptionsListComponent,
    ChildDaycareGroupInfoComponent,
    ChildBasicInfoComponent,
    DaycareGroupBasicInfoComponent,
    DaycareGroupChildrenListComponent,
    UserCreateAccountComponent,
    UserPageComponent,
    UserAssignDaycareGroupComponent,
    GroupSupervisorsListComponent,
    UserChangePasswordComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    ModalModule.forRoot(),
    BsDatepickerModule.forRoot(),
    AlertModule.forRoot(),
    BsDropdownModule.forRoot()
  ],
  entryComponents: [
    InformationModalComponent,
    ConfirmationModalComponent,
    ChildCreateEditComponent,
    ChildAssignOptionComponent,
    AssignToGroupComponent,
    CateringOptionCreateEditComponent,
    DaycareGroupCreateEditComponent,
    BillPreviewComponent,
    BillDisplayComponent,
    UserCreateAccountComponent,
    UserAssignDaycareGroupComponent,
    UserChangePasswordComponent
  ],
  providers: [
    DatePipe,
    { provide: HTTP_INTERCEPTORS, useClass: HttpInterceptorService, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
