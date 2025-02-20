import { Component } from '@angular/core';
import { UserService } from "./services/user.service";
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'qgen';
  constructor(private userService: UserService, private router: Router) {
    console.log("User's token: "+this.userService.cookieService.get('token'));
    if(this.userService.cookieService.get('username')!=""){
      this.userService.authenticated=true;
    }

    //Redirect to the last path in case of refreshing.
    if(this.userService.cookieService.get('lastPage')!=""){
      this.router.navigateByUrl(this.userService.cookieService.get('lastPage'));
    }
  }

  authenticated() {
    return this.userService.authenticated;
  }

  logout() {
    console.log("logout "+this.userService.cookieService.get('username'));
    this.userService.authenticated=false;
    this.userService.logout(this.userService.cookieService.get('username')).subscribe(
      data => {
        console.log("-Done.");
      },
      err => {
        console.log("-Error: "+err.error.message);
      }
    );
    this.userService.cookieService.delete( 'username' , '/' );
    this.userService.cookieService.delete( 'token' , '/' );
    this.router.navigateByUrl('/login');
  }
}
