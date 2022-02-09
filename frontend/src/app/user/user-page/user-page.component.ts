import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { JwtAuthService } from 'src/app/service/authentication/jwt-auth.service';
import { UserDataService } from 'src/app/service/data/user-data.service';
import { ChangePasswordModalComponent } from '../change-password-modal/change-password-modal.component';
import { User } from '../user';

@Component({
  selector: 'app-user-page',
  templateUrl: './user-page.component.html',
  styleUrls: ['./user-page.component.scss']
})
export class UserPageComponent implements OnInit {

  user: User

  constructor(
    private authService: JwtAuthService,
    private userDataService: UserDataService,
    private dialog: MatDialog,
    private snackbar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.retrieveYourProfile(this.authService.getUsername())
  }

  retrieveYourProfile(username: string) {
    this.userDataService.getSingleByUsername(username).subscribe(
      user => this.user = user
    )
  }

  changePassword() {
    const dialogRef = this.dialog.open(ChangePasswordModalComponent, {
      disableClose: true,
      panelClass: 'dcbg-dialog',
      data:
      {
        user: this.user
      }
    })
    dialogRef.afterClosed().subscribe(
      username => {
        if (username)
          this.retrieveYourProfile(username)
      })

  }

}
