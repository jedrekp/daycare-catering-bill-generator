import { Component, OnInit } from '@angular/core';
import { AppUser } from '../appUser';
import { AppUserDataService } from '../app-user-data.service';
import { ErrorHandlerService } from 'src/app/error/error-handler.service';
import { ActivatedRoute } from '@angular/router';


@Component({
  selector: 'app-app-user-page',
  templateUrl: './app-user-page.component.html',
  styleUrls: ['./app-user-page.component.css']
})
export class AppUserPageComponent implements OnInit {

  private appUser: AppUser;

  constructor(
    private route: ActivatedRoute,
    private appUserDataService: AppUserDataService,
    private errorHandlerService: ErrorHandlerService
  ) { }

  ngOnInit() {
    this.retrieveAppUser(this.route.snapshot.params['username'])
  }

  retrieveAppUser(username: string) {
    this.appUserDataService.getAppUserByUsername(username).subscribe(
      appUser => {
        this.appUser = appUser
      },
      err => this.errorHandlerService.redirectToErrorPage(err)
    )
  }

}
