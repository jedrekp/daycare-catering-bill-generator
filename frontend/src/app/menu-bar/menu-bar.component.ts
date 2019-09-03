import { Component, OnInit } from '@angular/core';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { Router } from '@angular/router';

import { Child } from '../child/child';
import { ChildCreateEditComponent } from '../child/child-create-edit/child-create-edit.component';
import { CateringOption } from '../catering-option/CateringOption';
import { CateringOptionCreateEditComponent } from '../catering-option/catering-option-create-edit/catering-option-create-edit.component';

@Component({
  selector: 'app-menu-bar',
  templateUrl: './menu-bar.component.html',
  styleUrls: ['./menu-bar.component.css']
})
export class MenuBarComponent implements OnInit {

  public modalRef: BsModalRef

  constructor(
    private router: Router,
    private modalService: BsModalService
  ) {
    this.router.routeReuseStrategy.shouldReuseRoute = function () {
      return false
    }
  }

  ngOnInit() { }

  openCreateChildModal() {
    let initialState = { child: new Child(-1, '', '') }
    this.modalRef = this.modalService.show(ChildCreateEditComponent,
      { class: 'modal-top-20 modal-sm', initialState, ignoreBackdropClick: true })
    this.modalRef.content.onClose.subscribe(
      childId => {
        if (childId) {
          this.router.navigate(['child-page', childId])
        }
      })
  }

  openCreateCateringOptionModal() {
    let initialState = { cateringOption: new CateringOption(-1, '', 0.00, false) }
    this.modalRef = this.modalService.show(CateringOptionCreateEditComponent,
      { class: 'modal-top-20 modal-sm', initialState, ignoreBackdropClick: true })
    this.modalRef.content.onClose.subscribe(
      onClose => {
        if (onClose) {
          this.router.navigate(['catering-options-list'])
        }
      })
  }
  
}
