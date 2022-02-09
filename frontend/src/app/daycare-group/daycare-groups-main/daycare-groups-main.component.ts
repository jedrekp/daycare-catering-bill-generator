import { Component, OnInit } from '@angular/core';
import { JwtAuthService } from 'src/app/service/authentication/jwt-auth.service';

@Component({
  selector: 'app-daycare-groups-main',
  templateUrl: './daycare-groups-main.component.html',
  styleUrls: ['./daycare-groups-main.component.scss']
})
export class DaycareGroupsMainComponent implements OnInit {

  constructor(
    public authService: JwtAuthService
  ) { }

  ngOnInit(): void {
  }

}
