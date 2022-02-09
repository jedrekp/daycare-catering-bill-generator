import { NgModule } from '@angular/core';
import { CateringBillsMainComponent } from './catering-bills-main/catering-bills-main.component';
import { CateringBillsListComponent } from './catering-bills-list/catering-bills-list.component';
import { SelectMonthAndGroupComponent } from './select-month-and-group/select-month-and-group.component';
import { SharedModule } from '../shared.module';
import { BillPreviewComponent } from './bill-preview/bill-preview.component';
import { SendingBillComponent } from './sending-bill.component/sending-bill.component';
import { CateringBillDisplayComponent } from './catering-bill-display/catering-bill-display.component';



@NgModule({
  declarations: [
    CateringBillsMainComponent,
    CateringBillsListComponent,
    SelectMonthAndGroupComponent,
    BillPreviewComponent,
    SendingBillComponent,
    CateringBillDisplayComponent
  ],
  imports: [
    SharedModule
  ]
})
export class CateringBillModule { }
