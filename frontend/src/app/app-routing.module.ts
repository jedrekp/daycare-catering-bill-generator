import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ChildPageComponent } from './child/child-page/child-page.component';
import { CateringOptionsListComponent } from './catering-option/catering-options-list/catering-options-list.component';


const routes: Routes = [
  { path: 'child-page/:childId', component: ChildPageComponent },
  { path: 'catering-options-list', component: CateringOptionsListComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
