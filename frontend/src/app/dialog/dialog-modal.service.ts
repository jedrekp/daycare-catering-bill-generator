import { Injectable } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { InformationModalComponent } from './information-modal/information-modal.component';
import { ConfirmationModalComponent } from './confirmation-modal/confirmation-modal.component';

@Injectable({
  providedIn: 'root'
})
export class DialogModalService {

  constructor(private bsModalService: BsModalService) { }

  openInformationModal(infoTitle: string, infoMessage: string) {
    let initialState = { title: infoTitle, message: infoMessage }
    return this.bsModalService.show(InformationModalComponent,
      { class: 'modal-top-10 modal-md', initialState, ignoreBackdropClick: true })
  }

  openNestedInformationModal(infoTitle: string, infoMessage: string) {
    let initialState = { title: infoTitle, message: infoMessage }
    return this.bsModalService.show(InformationModalComponent,
      { class: 'modal-top-25 modal-md', initialState, ignoreBackdropClick: true })
  }

  openConfirmationModal(confirmationTitle: string, confirmationMessage: string) {
    let initialState = { title: confirmationTitle, message: confirmationMessage }
    return this.bsModalService.show(ConfirmationModalComponent,
      { class: 'modal-top-10 modal-md', initialState, ignoreBackdropClick: true })
  }

}
