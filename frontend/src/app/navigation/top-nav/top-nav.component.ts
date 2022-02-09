import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { ThemeSwitchService } from 'src/app/service/theme-switch.service';

@Component({
  selector: 'app-top-nav',
  templateUrl: './top-nav.component.html',
  styleUrls: ['./top-nav.component.scss']
})
export class TopNavComponent implements OnInit {

  @Input('inputSidenav') sidenav: MatSidenav;
  @Output('sidenavToggled') sidenavToggled: EventEmitter<any> = new EventEmitter()

  darkModeOn: boolean

  constructor(
    private themeSwitchService: ThemeSwitchService,
  ) { }

  ngOnInit(): void {
    this.darkModeOn = this.themeSwitchService.isDarkTheme()
    this.toggleDarkMode()
  }

  toggleDarkMode() {
    this.themeSwitchService.setNewTheme(this.darkModeOn)
  }

  toggleSidenav() {
    this.sidenavToggled.emit(true)
  }

}