import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Database } from '../models/database';
import { DatabaseTable } from '../models/databaseTable';
import { DatabaseField } from '../models/databaseField';

@Injectable({
  providedIn: 'root'
})
export class FileService {
  constructor(private http: HttpClient) { }

  selectedFiles: FileList;
  currentFile: File;
  progress = 0;
  message = '';

  fileInfos: Observable<any>;

  filename : string = "";
  conditions : string = "";
  databasesToExport : Database[] = [];
  tablesToExport : DatabaseTable[] = [];
  fieldsToExport : DatabaseField[] = [];

  upload(file: File): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();

    formData.append('file', file);

    const req = new HttpRequest('POST', `upload`, formData, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }

  deleteDatabase(filename: string): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();

    formData.append('filename', filename);

    const req = new HttpRequest('DELETE', `delete/database`, formData, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }

  deleteQuery(filename: string): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();

    formData.append('filename', filename);

    const req = new HttpRequest('DELETE', `delete/query`, formData, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }
  
  getQueries(): Observable<any> {
    return this.http.get(`queries`);
  }

  getDatabases(): Observable<any> {
    return this.http.get(`databases`);
  }

  getFile(filename: string): Observable<any> {
    console.log("trying to get the file 2");
    return this.http.get(`files/${filename}`);
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
  }

  genFile(): Observable<any>{
    //Se debe comprobar antes la integridad del array fieldsToExport.
    console.log("generating file ");
    return this.http.post(`gen`,[this.filename,this.databasesToExport,this.tablesToExport,this.fieldsToExport]);
  }

  getDatabase(fileurl: string): Observable<any> {
    console.log("trying to get the database");
    const headers = new HttpHeaders({ 'Content-Type': 'application/json', responseType : 'blob'});
    return this.http.get<any>(`database/${fileurl}`,{ headers : headers, responseType : 'blob' as 'json'});
  }

  getQuery(fileurl: string): Observable<any> {
    console.log("trying to get the query");
    const headers = new HttpHeaders({ 'Content-Type': 'application/json', responseType : 'blob'});
    return this.http.get<any>(`queries/${fileurl}`,{ headers : headers, responseType : 'blob' as 'json'});
  }

  saveFile(filename: string){
    console.log("downloading file "+filename);
    this.getQuery(filename).subscribe(
      data => {
        console.log("Ok, got the file.");
        /* Open file without filename
        var file = new File([data], filename, { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        var fileURL = URL.createObjectURL(file);
        window.open(fileURL); */

        // Open file with filename
        var blob: Blob = new Blob([data]);
		const nav = (window.navigator as any);
        if(nav.msSaveOrOpenBlob){ //For IE & Edge
          nav.msSaveBlob(blob,filename);
        } else{ //For other browsers
          var objectUrl: string = URL.createObjectURL(blob);
          var a: HTMLAnchorElement = document.createElement('a') as HTMLAnchorElement;
          a.href = objectUrl;
          a.download = filename;
          document.body.appendChild(a);
          a.click();
          document.body.removeChild(a);
          URL.revokeObjectURL(objectUrl);
        }
      },
      err => {
        this.message = 'Could not get the file!';
        console.log(err);
      });
  }
}
