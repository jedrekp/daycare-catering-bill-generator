import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css']
})
export class ErrorComponent implements OnInit {

  private message: string = 'Requested page does not exist.'

  constructor(
    private router: Router
  ) {
    if (this.router.getCurrentNavigation().extras.state) {
      this.message = this.router.getCurrentNavigation().extras.state.message
    }
  }

  ngOnInit() { }

}
