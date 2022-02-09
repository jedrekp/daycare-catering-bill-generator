import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-error-page',
  templateUrl: './error-page.component.html',
  styleUrls: ['./error-page.component.scss']
})
export class ErrorPageComponent implements OnInit {

  message = 'An unexpected error occured.'

  constructor(
    router: Router
  ) {
    const state = router.getCurrentNavigation().extras.state
    if (state?.message)
      this.message = state.message
  }

  ngOnInit(): void {

  }

}