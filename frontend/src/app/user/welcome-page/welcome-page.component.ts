import { Component, OnInit, ErrorHandler } from '@angular/core';
import { UserDataService } from '../user-data.service';
import { ErrorHandlerService } from 'src/app/error/error-handler.service';
import { User } from '../user';
import { JwtAuthenticationService } from 'src/app/authentication/jwt-authentication.service';

@Component({
  selector: 'app-welcome-page',
  templateUrl: './welcome-page.component.html',
  styleUrls: ['./welcome-page.component.css']
})
export class WelcomePageComponent implements OnInit {

  private user: User;

  constructor(
    private userDataService: UserDataService,
    private authenticationService: JwtAuthenticationService,
    private errorHandlerService: ErrorHandlerService
  ) { }

  ngOnInit() {
    this.retrieveUser()
  }

  retrieveUser() {
    this.userDataService.getUserByUsername(this.authenticationService.getUsername()).subscribe(
      user => {
        this.user = user
      },
      err => {
        this.errorHandlerService.redirectToErrorPage(err)
      })
  }

}
