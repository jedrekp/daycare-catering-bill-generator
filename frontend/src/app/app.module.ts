import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ModalModule } from 'ngx-bootstrap/modal';
import { AlertModule } from 'ngx-bootstrap/alert';
import { AppRoutingModule } from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { TabsModule } from 'ngx-bootstrap/tabs';
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
    TabsModule.forRoot(),
    AlertModule.forRoot(),
    BsDropdownModule.forRoot()
  ],
  entryComponents: [
    ChildCreateEditComponent,
    ChildAssignOptionComponent,
    AssignToGroupComponent,
    CateringOptionCreateEditComponent
  ],
  providers: [DatePipe],
  bootstrap: [AppComponent]
})
export class AppModule { }
