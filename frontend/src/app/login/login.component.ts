import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { RETURN_URL } from 'src/const';
import { UnauthorizedError } from '../common/error/unauthorized-error';
import { JwtAuthService } from '../service/authentication/jwt-auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup
  invalidLogin: boolean = false;
  returnUrl: string

  constructor(
    private authService: JwtAuthService,
    private router: Router,
    private route: ActivatedRoute,
    private snackbar: MatSnackBar,
    fb: FormBuilder
  ) {
    this.loginForm = fb.group({
      username: fb.control('', Validators.required),
      password: fb.control('', Validators.required)
    })
  }

  ngOnInit(): void {
    this.returnUrl = this.route.snapshot.queryParamMap.get(RETURN_URL)
  }

  get usernameControl() {
    return this.loginForm.get('username')
  }

  get passwordControl() {
    return this.loginForm.get('password')
  }

  handleLogin() {
    if (this.loginForm.valid) {
      const credentials = this.loginForm.getRawValue()
      this.authService.authenticate(credentials).subscribe(
        () => this.router.navigate([this.returnUrl || '']),
        err => {
          if (err instanceof UnauthorizedError)
            this.snackbar.open('Invalid credentials.', null, { panelClass: 'dcbg-error-snackbar' })
          else
            throw Error
        })
    }
  }

}
