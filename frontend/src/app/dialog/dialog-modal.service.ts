import { Injectable } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { InformationModalComponent } from './information-modal/information-modal.component';

@Injectable({
  providedIn: 'root'
})
export class DialogModalService {

  constructor(private modalService: BsModalService) { }

  openInformationModal(infoTitle: string, infoMessage: string) {
    let initialState = { title: infoTitle, message: infoMessage }
    return this.modalService.show(InformationModalComponent,
      { class: 'modal-top-10 modal-md', initialState, ignoreBackdropClick: true })
  }

  openNestedInformationModal(infoTitle: string, infoMessage: string) {
    let initialState = { title: infoTitle, message: infoMessage }
    return this.modalService.show(InformationModalComponent,
      { class: 'modal-top-25 modal-md', initialState, ignoreBackdropClick: true })
  }
}
