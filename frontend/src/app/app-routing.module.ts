import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ChildCreateEditComponent } from './child/child-create-edit/child-create-edit.component';
import { ChildPageComponent } from './child/child-page/child-page.component';


const routes: Routes = [
  { path: 'child-create-edit/:childId', component: ChildCreateEditComponent },
  { path: 'child-page/:childId', component: ChildPageComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
