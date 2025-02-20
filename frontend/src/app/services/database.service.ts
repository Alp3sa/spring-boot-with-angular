import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Database } from '../models/database';

@Injectable({
  providedIn: 'root'
})
export class DatabaseService {
  private baseUrl = '/database';

  constructor(private http: HttpClient) { }

  /*get(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/get/${id}`);
  }

  create(database: Object): Observable<Object> {
    return this.http.post(`${this.baseUrl}/post`, database);
  }

  update(id: number, database: Object): Observable<Object> {
    return this.http.put(`${this.baseUrl}/put/${id}`, database);
  }

  delete(id: number): Observable<any> {
    console.log("service trying to delete...");
    return this.http.delete(`${this.baseUrl}/delete/${id}`);
  }*/

  getList(): Observable<Database[]> {
    return this.http.get<Database[]>(`${this.baseUrl}/getListDatabases`);
  }
}