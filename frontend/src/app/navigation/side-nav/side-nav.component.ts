import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { Router } from '@angular/router';
import { JwtAuthService } from 'src/app/service/authentication/jwt-auth.service';

@Component({
  selector: 'app-side-nav',
  templateUrl: './side-nav.component.html',
  styleUrls: ['./side-nav.component.scss']
})
export class SideNavComponent implements OnInit {

  @Input('sidenav') sidenav: MatSidenav
  @Output('sidenavToggled') sidenavToggled: EventEmitter<any> = new EventEmitter()

  constructor(
    public authService: JwtAuthService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
  }

  logout() {
    this.authService.logout()
    this.router.navigate(['login'])
  }

  toggleSidenav() {
    this.sidenavToggled.emit(true)
  }

}
