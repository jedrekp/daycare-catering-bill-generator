import { Component, OnInit } from '@angular/core';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { Router } from '@angular/router';


import { Child } from '../child/child';
import { ChildCreateEditComponent } from '../child/child-create-edit/child-create-edit.component';
import { CateringOption } from '../catering-option/CateringOption';
import { CateringOptionCreateEditComponent } from '../catering-option/catering-option-create-edit/catering-option-create-edit.component';
import { DaycareGroup } from '../daycare-group/daycare-group';
import { DaycareGroupCreateEditComponent } from '../daycare-group/daycare-group-create-edit/daycare-group-create-edit.component';
import { JwtAuthenticationService } from '../authentication/jwt-authentication.service';
import { DialogModalService } from '../dialog/dialog-modal.service';
import { ACTION_COMPLETED_HEADER } from '../const';

@Component({
  selector: 'app-menu-bar',
  templateUrl: './menu-bar.component.html',
  styleUrls: ['./menu-bar.component.css']
})
export class MenuBarComponent implements OnInit {

  private modalRef: BsModalRef
  private navbarOpen: boolean
  private searchPhrase: String

  constructor(
    private router: Router,
    private modalService: BsModalService,
    private dialogModalService: DialogModalService,
    private authenticationService: JwtAuthenticationService
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
    this.router.navigated = false
    this.router.navigate([route])
  }

  openCreateChildModal() {
    let initialState = { child: new Child(-1, '', '', '', false) }
    this.modalRef = this.modalService.show(ChildCreateEditComponent,
      { class: 'modal-top-10 modal-sm', initialState, ignoreBackdropClick: true })
    this.modalRef.content.onClose.subscribe(
      childId => {
        if (childId) {
          this.router.navigated = false
          this.router.navigate(['child-page', childId])
          this.closeNavbar()
          this.searchPhrase = null
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
          this.searchPhrase = null
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
          this.router.navigate(['daycare-group-page', groupId])
          this.closeNavbar()
          this.searchPhrase = null
        }
      })
  }

  findChildren() {
    if (this.searchPhrase) {
      this.router.navigated = false
      this.router.navigate(['children-search-results', this.searchPhrase])
      this.closeNavbar()
      this.searchPhrase = null
    }
  }

  logout() {
    this.authenticationService.logout()
    this.modalRef = this.dialogModalService.openInformationModal(ACTION_COMPLETED_HEADER,'You are now logged out.')
    this.modalRef.content.onClose.subscribe(
      onClose => {
        this.router.navigate(['login'])
        this.closeNavbar()
      })
  }

}
