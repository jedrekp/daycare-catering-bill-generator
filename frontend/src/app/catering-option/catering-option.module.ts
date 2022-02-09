import { NgModule } from '@angular/core';
import { CateringOptionsMainComponent } from './catering-options-main/catering-options-main.component';
import { CreateCateringOptionFormComponent } from './create-catering-option-form/create-catering-option-form.component';
import { CateringOptionsListComponent } from './catering-options-list/catering-options-list.component';
import { ActiveCateringOptionsComponent } from './active-catering-options/active-catering-options.component';
import { DisabledCateringOptionsComponent } from './disabled-catering-options/disabled-catering-options.component';
import { SharedModule } from '../shared.module';
import { EditCateringOptionModalComponent } from './edit-catering-option-modal/edit-catering-option-modal.component';



@NgModule({
  declarations: [
    CateringOptionsMainComponent,
    CreateCateringOptionFormComponent,
    CateringOptionsListComponent,
    ActiveCateringOptionsComponent,
    DisabledCateringOptionsComponent,
    EditCateringOptionModalComponent
  ],
  imports: [
    SharedModule
  ]
})
export class CateringOptionModule { }
