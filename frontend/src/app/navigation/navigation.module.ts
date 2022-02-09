import { NgModule } from '@angular/core';
import { SharedModule } from '../shared.module';
import { SideNavComponent } from './side-nav/side-nav.component';
import { TopNavComponent } from './top-nav/top-nav.component';



@NgModule({
  declarations: [
    SideNavComponent,
    TopNavComponent
  ],
  imports: [
    SharedModule
  ],
  exports:[
    SideNavComponent,
    TopNavComponent
  ]
})
export class NavigationModule { }
