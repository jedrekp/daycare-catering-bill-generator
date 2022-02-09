import { Component, OnInit } from '@angular/core';
import { JwtAuthService } from 'src/app/service/authentication/jwt-auth.service';

@Component({
  selector: 'app-child-profiles-main',
  templateUrl: './child-profiles-main.component.html',
  styleUrls: ['./child-profiles-main.component.scss']
})
export class ChildProfilesMainComponent implements OnInit {

  constructor(
    public authService: JwtAuthService
  ) { }

  ngOnInit(): void {
  }

}
