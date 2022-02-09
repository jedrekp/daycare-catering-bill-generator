import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NotFoundError } from 'src/app/common/error/not-found-error';
import { ChildDataService } from 'src/app/service/data/child-data.service';
import { Child } from '../child';


@Component({
  selector: 'app-child-profile',
  templateUrl: './child-profile.component.html',
  styleUrls: ['./child-profile.component.scss'],

})
export class ChildProfileComponent implements OnInit {

  child: Child

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private childDataService: ChildDataService
  ) { }

  ngOnInit(): void {
    this.retrieveChild(this.route.snapshot.params['id'])
  }

  retrieveChild(id: number) {
    this.childDataService.getSingle(id).subscribe(
      child => this.child = child,
      err => {
        if (err instanceof NotFoundError)
          this.router.navigate(['error'], { state: { message: err.originalError.error } })
        else throw err
      })
  }

}

