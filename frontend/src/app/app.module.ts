import { ErrorHandler, NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { JwtModule } from '@auth0/angular-jwt';
import { TOKEN } from 'src/const';
import { DefaultErrorHandler } from './common/error/default-error-handler';
import { UserDialogComponent } from './user-dialog/user-dialog.component';
import { DatePipe } from '@angular/common';
import { MAT_SNACK_BAR_DEFAULT_OPTIONS } from '@angular/material/snack-bar';
import { SharedModule } from './shared.module';
import { NavigationModule } from './navigation/navigation.module';
import { DaycareGroupModule } from './daycare-group/daycare-group.module';
import { ChildModule } from './child/child.module';
import { UserModule } from './user/user.module';
import { ErrorPageComponent } from './common/error/error-page/error-page.component';
import { CateringOptionModule } from './catering-option/catering-option.module';
import { AttendanceModule } from './attendance/attendance.module';
import { CateringBillModule } from './catering-bill/catering-bill.module';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    UserDialogComponent,
    ErrorPageComponent
  ],
  imports: [
    SharedModule,
    NavigationModule,
    DaycareGroupModule,
    ChildModule,
    CateringOptionModule,
    UserModule,
    AttendanceModule,
    CateringBillModule,
    JwtModule.forRoot({
      config: {
        tokenGetter: () => {
          return localStorage.getItem(TOKEN);
        },
        allowedDomains: ['localhost:8081'],
        disallowedRoutes: ['localhost:8081/authenticate']
      }
    }),
  ],
  providers: [
    DatePipe,
    { provide: ErrorHandler, useClass: DefaultErrorHandler },
    { provide: MAT_SNACK_BAR_DEFAULT_OPTIONS, useValue: { duration: 4000, panelClass: 'dcbg-snackbar' } }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
