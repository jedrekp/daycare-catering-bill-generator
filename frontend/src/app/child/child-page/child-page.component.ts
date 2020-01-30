import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ChildDataService } from '../child-data.service';
import { Child } from '../child';
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
