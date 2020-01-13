import { Component, OnInit } from '@angular/core';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { ActivatedRoute, Router } from '@angular/router';
import { ChildDataService } from '../child-data.service';
import { Child } from '../child';
import { ChildCreateEditComponent } from '../child-create-edit/child-create-edit.component';
import { DialogModalService } from 'src/app/dialog/dialog-modal.service';
import { CONFIRMATION_HEADER, ACTION_COMPLETED_HEADER, ERROR_HEADER } from 'src/app/const';
import { JwtAuthenticationService } from 'src/app/authentication/jwt-authentication.service';
import { ErrorHandlerService } from 'src/app/error/error-handler.service';

@Component({
  selector: 'app-child-page',
  templateUrl: './child-page.component.html',
  styleUrls: ['./child-page.component.css']
})
export class ChildPageComponent implements OnInit {

  private child: Child

  constructor(
    private route: ActivatedRoute,
    private childDataService: ChildDataService,
    private errorHandlerService: ErrorHandlerService
  ) { }

  ngOnInit() {
    this.retrieveChild(this.route.snapshot.params['childId'])
  }

  retrieveChild(childId: number) {
    this.childDataService.retrieveChild(childId).subscribe(
      child => {
        this.child = child
      },
      err => {
        this.errorHandlerService.redirectToErrorPage(err)
      })
  }

}

