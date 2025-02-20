import { Component, OnInit } from '@angular/core';
import { User } from '../models/user';
import { UserService } from "./../services/user.service";
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  credentials: User;

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit(): void {
    this.credentials = {
      id: null,
      username: "",
      email: "",
      password: "",
      password2: ""
    };

    /*if(this.userService.authenticated){
      this.router.navigateByUrl(this.userService.cookieService.get('lastPage'));
    }*/

    if (this.userService.getToken()) {
      this.isLoggedIn = true;
      this.roles = this.userService.getUser().roles;
    }

    this.userService.cookieService.set('lastPage','/login');
  }

  form: any = {
    username: null,
    password: null
  };
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];

  login() {
    this.userService.login(this.credentials.username, this.credentials.password).subscribe(
      data => {
        if(data.message!=undefined){
          this.errorMessage = data.message;
          this.isLoginFailed = true;
          this.userService.authenticated = false;
          return;
        }
        this.userService.cookieService.set('username',this.credentials.username);
        this.userService.cookieService.set('token',data.accessToken);
        this.userService.saveToken(data.accessToken);
        this.userService.saveUser(data);
        
        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.userService.authenticated = true;
        this.roles = this.userService.getUser().roles;
        this.router.navigate(['/uploadFile'], {queryParams: {"welcome": this.credentials.username}});
      },
      err => {
        console.log("("+err.error.message+")");
        this.errorMessage = "Rellene todos los campos.";
        if(err.error.message){
          this.errorMessage = err.error.message;
        }
        this.isLoginFailed = true;
        this.userService.authenticated = false;
      }
    );
  }
}
