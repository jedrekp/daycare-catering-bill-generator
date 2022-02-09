import { ChangeDetectorRef, Component, HostListener, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { TopNavComponent } from './navigation/top-nav/top-nav.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'front-end';

  @ViewChild(MatSidenav) sidenav: MatSidenav;

  @ViewChild(TopNavComponent) topNav: TopNavComponent;

  sidenavManualOpen: boolean

  constructor(
    private cdRef: ChangeDetectorRef) {
  }

  ngOnInit() {
  }

  ngAfterContentChecked() {
    this.cdRef.detectChanges()
    this.configureSideNav()
  }

  @HostListener('window:resize', ['$event'])
  onResize() {
    this.configureSideNav()
  }

  configureSideNav() {
    let smallScreen = window.innerWidth < 768
    if (smallScreen) {
      this.sidenav.mode = 'over'
      if (!this.sidenavManualOpen) {
        this.sidenav.opened = false
      }
    } else {
      this.sidenavManualOpen = false
      this.sidenav.mode = "side"
      this.sidenav.opened = true
    }
  }

  toggleSidenav() {
    if (this.sidenav.mode === 'over') {
      this.sidenavManualOpen = !this.sidenavManualOpen
      this.sidenav.toggle()
    }
  }

}
