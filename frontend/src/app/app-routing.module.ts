import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { UploadFilesComponent } from './upload-files/upload-files.component';
import { CreateFileComponent } from './create-file/create-file.component';
import { GetFilesComponent } from './get-files/get-files.component';
import { NgModule, ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'uploadFile', component: UploadFilesComponent },
  { path: 'createFile', component: CreateFileComponent },
  { path: 'getFiles', component: GetFilesComponent }
];
export const appRouting: ModuleWithProviders<any> = RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' });

@NgModule({
  imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }