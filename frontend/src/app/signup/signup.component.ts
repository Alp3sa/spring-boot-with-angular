import { Component, OnInit } from '@angular/core';
import { User } from '../models/user';
import { UserService } from "./../services/user.service";
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
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

    if(this.userService.authenticated){
      this.router.navigateByUrl('/uploadFile');
    }
    this.userService.cookieService.set('lastPage','/signup');
  }

  form: any = {
    username: null,
    password: null
  };
  isLoggedIn = false;
  isSignupFailed = false;
  errorMessage = '';
  roles: string[] = [];

  signup() {
    console.log("signup");
    if(this.credentials.password!==this.credentials.password2){
      this.errorMessage="Las contraseñas introducidas no coinciden.";
      this.isSignupFailed = true;
      return;
    }
    this.userService.signup(this.credentials.username, this.credentials.email, this.credentials.password).subscribe(
      data => {
        if(data.message!=undefined){
          this.errorMessage = data.message;
          this.isSignupFailed = true;
          this.userService.authenticated = false;
          return;
        }
        this.userService.cookieService.set('username',this.credentials.username);
        this.userService.cookieService.set('token',data.accessToken);
        this.userService.saveToken(data.accessToken);
        this.userService.saveUser(data);

        this.isSignupFailed = false;
        this.isLoggedIn = true;
        this.userService.authenticated = true;
        this.roles = this.userService.getUser().roles;
        this.router.navigate(['/uploadFile'], {queryParams: {"welcome": this.credentials.username}});
      },
      err => {
        this.errorMessage = "Rellene todos los campos.";
        if(err.error.message){
          this.errorMessage = err.error.message;
        }
        this.isSignupFailed = true;
        this.userService.authenticated = false;
      }
    );
  }
}
