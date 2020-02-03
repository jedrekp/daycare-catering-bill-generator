import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { UserDataService } from '../user-data.service';
import { ErrorHandlerService } from 'src/app/error/error-handler.service';
import { ActivatedRoute } from '@angular/router';
import { JwtAuthenticationService } from 'src/app/authentication/jwt-authentication.service';


@Component({
  selector: 'app-user-page',
  templateUrl: './user-page.component.html',
  styleUrls: ['./user-page.component.css']
})
export class UserPageComponent implements OnInit {

  private user: User;

  constructor(
    private route: ActivatedRoute,
    private userDataService: UserDataService,
    private authenticationService: JwtAuthenticationService,
    private errorHandlerService: ErrorHandlerService
  ) { }

  ngOnInit() {
    this.retrieveAppUser(this.route.snapshot.params['username'])
  }

  retrieveAppUser(username: string) {
    this.userDataService.getUserByUsername(username).subscribe(
      user => {
        this.user = user
      },
      err => this.errorHandlerService.redirectToErrorPage(err)
    )
  }

  checkIfAccountBelongsToLoggedUser() {
    return this.authenticationService.getUsername() == this.user.username
  }

}
