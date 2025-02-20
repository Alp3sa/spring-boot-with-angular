import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { FileService } from "./../services/file.service";
import { HttpResponse } from '@angular/common/http';
import { UserService } from "./../services/user.service";
import { Router } from '@angular/router';

@Component({
  selector: 'app-get-files',
  templateUrl: './get-files.component.html',
  styleUrls: ['./get-files.component.css']
})
export class GetFilesComponent implements OnInit {

  currentFile: File;
  progress = 0;
  message = '';

  fileInfos: Observable<any>;
  isListOfFilesEmpty: boolean = false;

  constructor(private fileService: FileService, private userService: UserService, private router: Router) { }

  ngOnInit(): void {
    if(!this.userService.authenticated){
      this.router.navigateByUrl('/login');
    }
    this.fileInfos = this.fileService.getQueries();
    this.userService.cookieService.set('lastPage','/getFiles');
    this.listFilesIsEmpty();
  }

  deleteQuery(filename:string){
    this.currentFile = undefined;
    this.fileService.deleteQuery(filename).subscribe(
      event => {
        if (event instanceof HttpResponse) {
          this.message = event.body.message;
          this.fileInfos = this.fileService.getQueries();
          this.listFilesIsEmpty();
        }
      },
      err => {
        this.progress = 0;
        this.message = 'No se pudo borrar el fichero.';
        this.currentFile = undefined;
      });
  }

  getFile(filename:string){
    this.fileService.saveFile(filename);
  }

  listFilesIsEmpty(){
    this.fileInfos.subscribe(files =>{
      let listSize: number = (<any[]> files).length;
      if(listSize>0){
        this.isListOfFilesEmpty=false;
        return;
      }
      this.isListOfFilesEmpty=true;
    });
  }
}
