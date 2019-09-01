import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ModalModule } from 'ngx-bootstrap/modal';
import { AlertModule } from 'ngx-bootstrap/alert';
import { AppRoutingModule } from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { DatePipe } from '@angular/common';

import { AppComponent } from './app.component';
import { ChildCreateEditComponent } from './child/child-create-edit/child-create-edit.component';
import { MenuBarComponent } from './menu-bar/menu-bar.component';
import { ChildPageComponent } from './child/child-page/child-page.component';
import { ChildAssignOptionComponent } from './child/child-assign-option/child-assign-option.component';
import { AssignToGroupComponent } from './daycare-group/assign-to-group/assign-to-group.component';


@NgModule({
  declarations: [
    AppComponent,
    ChildCreateEditComponent,
    MenuBarComponent,
    ChildPageComponent,
    ChildAssignOptionComponent,
    AssignToGroupComponent,
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
    AlertModule.forRoot()
  ],
  entryComponents: [
    ChildCreateEditComponent,
    ChildAssignOptionComponent,
    AssignToGroupComponent
  ],
  providers: [DatePipe],
  bootstrap: [AppComponent]
})
export class AppModule { }
