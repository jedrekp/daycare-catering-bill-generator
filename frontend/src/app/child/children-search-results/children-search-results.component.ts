import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ChildDataService } from '../child-data.service';
import { Child } from '../child';
import { ErrorHandlerService } from 'src/app/error/error-handler.service';

@Component({
  selector: 'app-children-search-results',
  templateUrl: './children-search-results.component.html',
  styleUrls: ['./children-search-results.component.css']
})
export class ChildrenSearchResultsComponent implements OnInit {

  private children: Child[]

  constructor(
    private route: ActivatedRoute,
    private childDataService: ChildDataService,
    private errorHandlerService: ErrorHandlerService
  ) { }

  ngOnInit() {
    let searchPhrase = this.route.snapshot.params['searchPhrase']
    this.childDataService.findChildrenBySearchPhrase(searchPhrase).subscribe(
      children => {
        this.children = children
      },
      err => {
        this.errorHandlerService.redirectToErrorPage(err)
      })
  }

}
