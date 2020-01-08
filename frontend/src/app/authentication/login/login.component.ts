import { Component, OnInit } from '@angular/core';
import { JwtAuthenticationService } from '../jwt-authentication.service';
import { Router } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ErrorHandlerService } from 'src/app/error/error-handler.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  private loginForm: FormGroup
  private invalidLogin: boolean = false;

  constructor(
    private authenticationService: JwtAuthenticationService,
    private router: Router,
    private errorHandlerService: ErrorHandlerService
  ) { }

  ngOnInit() {
    this.loginForm = new FormGroup({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required]),
    })
  }

  handleLogin() {
    if (this.loginForm.valid) {
      let username = this.loginForm.get('username').value
      let password = this.loginForm.get('password').value
      this.authenticationService.performJwtAuthentication(username, password).subscribe(
        data => {
          this.invalidLogin = false;
          this.router.navigate(['welcome-page'])
        }, err => {
          if (err.status == 0) {
            this.errorHandlerService.redirectToErrorPage(err)
          } else {
            this.invalidLogin = true;
          }
        })
    }
  }

}
