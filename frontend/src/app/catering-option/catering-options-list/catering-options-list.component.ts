import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { CateringOption } from '../CateringOption';
import { CateringOptionDataService } from '../catering-option-data.service';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { CateringOptionCreateEditComponent } from '../catering-option-create-edit/catering-option-create-edit.component';

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
    private modalService: BsModalService
  ) { }

  ngOnInit() {
    this.switchToActive()
    this.retrieveCateringOptions()
  }

  retrieveCateringOptions() {
    this.cateringOptionDataService.retriveCateringOptionsByDisabled(false).subscribe(
      cateringOptions => {
        this.activeCateringOptions = cateringOptions
      })
    this.cateringOptionDataService.retriveCateringOptionsByDisabled(true).subscribe(
      cateringOptions => {
        this.disabledCateringOptions = cateringOptions
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
    let initialState = { cateringOption: cateringOptionToEdit }
    this.modalRef = this.modalService.show(CateringOptionCreateEditComponent,
      { class: 'modal-top-10 modal-sm', initialState, ignoreBackdropClick: true })
    this.modalRef.content.onClose.subscribe(
      onClose => {
        if (onClose) {
          this.retrieveCateringOptions();
        }
      })
  }

}



