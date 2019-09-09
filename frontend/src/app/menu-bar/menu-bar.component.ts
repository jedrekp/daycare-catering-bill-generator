import { Component, OnInit } from '@angular/core';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { Router } from '@angular/router';


import { Child } from '../child/child';
import { ChildCreateEditComponent } from '../child/child-create-edit/child-create-edit.component';
import { CateringOption } from '../catering-option/CateringOption';
import { CateringOptionCreateEditComponent } from '../catering-option/catering-option-create-edit/catering-option-create-edit.component';
import { DaycareGroup } from '../daycare-group/daycare-group';
import { DaycareGroupCreateEditComponent } from '../daycare-group/daycare-group-create-edit/daycare-group-create-edit.component';

@Component({
  selector: 'app-menu-bar',
  templateUrl: './menu-bar.component.html',
  styleUrls: ['./menu-bar.component.css']
})
export class MenuBarComponent implements OnInit {

  private navbarOpen: boolean;
  public modalRef: BsModalRef

  constructor(
    private router: Router,
    private modalService: BsModalService,
  ) {
    this.router.routeReuseStrategy.shouldReuseRoute = function () {
      return false
    }
  }

  ngOnInit() { }

  toggleNavbar() {
    this.navbarOpen = !this.navbarOpen
  }

  closeNavbar() {
    this.navbarOpen = false
  }

  navigateFromMenu(route: string) {
    this.closeNavbar()
    this.router.navigate([route])
  }

  openCreateChildModal() {
    let initialState = { child: new Child(-1, '', '', '') }
    this.modalRef = this.modalService.show(ChildCreateEditComponent,
      { class: 'modal-top-10 modal-sm', initialState, ignoreBackdropClick: true })
    this.modalRef.content.onClose.subscribe(
      childId => {
        if (childId) {
          this.router.navigated = false
          this.router.navigate(['child-page', childId])
          this.closeNavbar()
        }
      })
  }

  openCreateCateringOptionModal() {
    let initialState = { cateringOption: new CateringOption(-1, '', 0, false) };
    this.modalRef = this.modalService.show(CateringOptionCreateEditComponent,
      { class: 'modal-top-10 modal-sm', initialState, ignoreBackdropClick: true })
    this.modalRef.content.onClose.subscribe(
      onClose => {
        if (onClose) {
          this.router.navigated = false
          this.router.navigate(['catering-options-list'])
          this.closeNavbar()
        }
      })
  }

  openCreateDaycareGroupModal() {
    let initialState = { daycareGroup: new DaycareGroup(-1, '') }
    this.modalRef = this.modalService.show(DaycareGroupCreateEditComponent,
      { class: 'modal-top-10 modal-sm', initialState, ignoreBackdropClick: true })
    this.modalRef.content.onClose.subscribe(
      groupId => {
        if (groupId) {
          this.router.navigated = false
          this.router.navigate(['child-page', groupId])
          this.closeNavbar()
        }
      }
    )
  }

}
