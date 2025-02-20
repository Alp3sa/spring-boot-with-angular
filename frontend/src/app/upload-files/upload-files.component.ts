import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';
import { FileService } from "./../services/file.service";
import { UserService } from "./../services/user.service";
import { HttpEventType, HttpResponse } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-upload-files',
  templateUrl: './upload-files.component.html',
  styleUrls: ['./upload-files.component.css']
})
export class UploadFilesComponent implements OnInit {

  selectedFiles: FileList;
  currentFile: File;
  message = '';

  fileInfos: Observable<any>;

  @ViewChild('uploadInput')
  uploadInput: ElementRef;
  welcome: string;
  isListOfFilesEmpty: boolean = false;
  isLoading: boolean = false;
  isLoaded: boolean = false;
  loadingMessage: string ="";
  error: string = "";

  constructor(private uploadService: FileService, private userService: UserService, private router: Router, private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {
    if(!this.userService.authenticated){
      this.router.navigateByUrl('/login');
    }
    this.fileInfos = this.uploadService.getDatabases();
    this.userService.cookieService.set('lastPage','/uploadFile');

    this.activatedRoute.queryParams.subscribe(params => {
      this.welcome = params["welcome"];
    });
    this.listFilesIsEmpty();
  }

  selectFile(event) {
    this.resetLoadingVars();
    this.selectedFiles = event.target.files;
  }

  upload() {
    this.resetLoadingVars();
    if(!this.selectedFiles){
      this.message = 'Tienes que seleccionar un fichero primero.';
    }
    else{
      this.isLoading = true;
      this.loadingMessage = "Cargando...";
      this.currentFile = this.selectedFiles.item(0);
      this.uploadService.upload(this.currentFile).subscribe(
        event => {
          if (event.type === HttpEventType.UploadProgress) {
            this.loadingMessage = "Permanezca a la espera mientras se carga la base de datos. Esto puede tardar unos minutos...";
          } else if (event instanceof HttpResponse) {
            this.loadingMessage = "";
            this.message = event.body.message;
            this.uploadInput.nativeElement.value = "";
            this.fileInfos = this.uploadService.getDatabases();
            this.listFilesIsEmpty();
            this.isLoaded = true;
            console.log("completed");
          }
        },
        err => {
          this.error="Hubo un error al subir el fichero."
          this.message = err.error.message;
          this.isLoading = false;
          this.isLoaded = false;
          this.loadingMessage = "Hubo un error al subir el fichero";
          this.currentFile = undefined;
        });
    
      this.selectedFiles = undefined;
    }
  }
  deleteDatabase(filename:string){
    this.resetLoadingVars();
    this.message = "Permanezca a la espera mientras se elimina la base de datos. Este proceso puede tardar varios minutos.";
    this.uploadService.deleteDatabase(filename).subscribe(
      event => {
        if (event instanceof HttpResponse) {
          this.message = event.body.message;
          this.fileInfos = this.uploadService.getDatabases();
          this.listFilesIsEmpty();
        }
      },
      err => {
        this.message = 'No se pudo borrar el fichero.';
        this.currentFile = undefined;
      });
  }

  getFile(filename:string){
    console.log("downloading file "+filename);
    this.uploadService.getDatabase(filename).subscribe(
      data => {
        console.log("Ok, got the file.");
        /* Open file without filename
        var file = new File([data], filename, { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        var fileURL = URL.createObjectURL(file);
        window.open(fileURL); */
        var blob: Blob = new Blob([data]);
        if(window.navigator.msSaveOrOpenBlob){ //For IE & Edge
          window.navigator.msSaveBlob(blob,filename);
        } else{ //For other browsers
          var objectUrl = window.URL.createObjectURL(blob);
          var a = document.createElement("a");
          a.download = filename;
          a.href = objectUrl;
          a.id = "link";
          a.click();
          //document.body.removeChild(a);
          URL.revokeObjectURL(objectUrl);
        }
      },
      err => {
        this.message = 'No se pudo obtener el fichero.';
        console.log(err);
      });
  }

  listFilesIsEmpty(){
    if(this.fileInfos!==undefined){
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

  resetLoadingVars(){
    this.error="";
    this.welcome=null;
    this.currentFile = undefined;
    this.isLoading = false;
    this.isLoaded = false;
    this.loadingMessage ="";
    this.message = "";
  }
}
