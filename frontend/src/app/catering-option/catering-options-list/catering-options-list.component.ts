import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { CateringOption } from '../CateringOption';
import { CateringOptionDataService } from '../catering-option-data.service';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { CateringOptionCreateEditComponent } from '../catering-option-create-edit/catering-option-create-edit.component';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { JwtAuthenticationService } from 'src/app/authentication/jwt-authentication.service';
import { ERROR_HEADER } from 'src/app/const';
import { ErrorHandlerService } from 'src/app/error/error-handler.service';

@Component({
  selector: 'app-catering-options-list',
  templateUrl: './catering-options-list.component.html',
  styleUrls: ['./catering-options-list.component.css']
})
export class CateringOptionsListComponent implements OnInit {

  public modalRef: BsModalRef

  private activeCateringOptions: CateringOption[]
  private disabledCateringOptions: CateringOption[]
  private header: string
  private showDisabled: boolean

  constructor(
    private cateringOptionDataService: CateringOptionDataService,
    private dialogModalService: DialogModalService,
    private modalService: BsModalService,
    private authenticationService: JwtAuthenticationService,
    private errorHandlerService: ErrorHandlerService
  ) { }

  ngOnInit() {
    this.switchToActive()
    this.retrieveCateringOptions()
  }

  retrieveCateringOptions() {
    this.cateringOptionDataService.retreiveCateringOptionsByDisabled(false).subscribe(
      cateringOptions => {
        this.activeCateringOptions = cateringOptions
      },
      err => {
        this.errorHandlerService.redirectToErrorPage(err)
      })
    this.cateringOptionDataService.retreiveCateringOptionsByDisabled(true).subscribe(
      cateringOptions => {
        this.disabledCateringOptions = cateringOptions
      },
      err => {
        this.errorHandlerService.redirectToErrorPage(err)
      })
  }

  switchToDisabled() {
    this.showDisabled = true
    this.header = 'Disabled catering options'
  }

  switchToActive() {
    this.showDisabled = false
    this.header = 'Active catering options'
  }

  openEditCateringOptionModal(cateringOptionToEdit: CateringOption) {
    if (this.authenticationService.getUserRole() == 'HEADMASTER') {
      let initialState = { cateringOption: cateringOptionToEdit }
      this.modalRef = this.modalService.show(CateringOptionCreateEditComponent,
        { class: 'modal-top-10 modal-sm', initialState, ignoreBackdropClick: true })
      this.modalRef.content.onClose.subscribe(
        cateringOption => {
          if (cateringOption) {
            this.modalRef = this.dialogModalService.openInformationModal('Option edited',
              `Catering option #${cateringOption.id} has been succesfully edited.`)
            this.retrieveCateringOptions();
          }
        })
    } else {
      this.dialogModalService.openInformationModal(ERROR_HEADER, 'You are not authorized to perform this action.')
    }
  }

}



