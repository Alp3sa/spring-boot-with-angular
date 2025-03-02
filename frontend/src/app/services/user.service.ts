import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { CookieService } from 'ngx-cookie-service';
import { catchError } from 'rxjs/operators';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};
const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  authenticated = false;

  constructor(private http: HttpClient, public cookieService: CookieService) {
  }

  login(username: string, password: string): Observable<any> {
      console.log("trying login "+username);
    return this.http.post('api/auth/login', {
      username,
      password
    }, httpOptions).pipe(
	   catchError(error => {
		   console.error('login error:', error);
		   return throwError(error);
	   })
    );
  }

  signup(username: string, email: string, password: string): Observable<any> {
    return this.http.post('api/auth/signup', {
      username,
      email,
      password
    }, httpOptions).pipe(
	   catchError(error => {
		   console.error('signup error:', error);
		   return throwError(error);
	   })
    );
  }

  logout(username: string): Observable<any> {
    console.log("trying logout "+username);
    window.sessionStorage.clear();
    return this.http.post('api/auth/logout', {
      username
    }, httpOptions);
  }

  //Tokens management

  public saveToken(token: string): void {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token);
  }

  public getToken(): string | null {
    return window.sessionStorage.getItem(TOKEN_KEY);
  }

  public saveUser(user: any): void {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  public getUser(): any {
    const user = window.sessionStorage.getItem(USER_KEY);
    if (user) {
      return JSON.parse(user);
    }

    return {};
  }
}