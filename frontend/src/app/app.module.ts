import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CookieService } from 'ngx-cookie-service';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { UploadFilesComponent } from './upload-files/upload-files.component';
import { CreateFileComponent } from './create-file/create-file.component';
import { GetFilesComponent } from './get-files/get-files.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { AuthInterceptorProviders } from './app-auth.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    UploadFilesComponent,
    CreateFileComponent,
    GetFilesComponent,
    LoginComponent,
    SignupComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
	  FormsModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [
    CookieService,
    AuthInterceptorProviders
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
