import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DatabaseField } from '../models/databaseField';

@Injectable({
  providedIn: 'root'
})
export class DatabaseFieldService {
  private baseUrl = '/databaseField';

  constructor(private http: HttpClient) { }

  get(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/get/${id}`);
  }

  create(databaseField: Object): Observable<Object> {
    return this.http.post(`${this.baseUrl}/post`, databaseField);
  }

  update(id: number, databaseField: Object): Observable<Object> {
    return this.http.put(`${this.baseUrl}/put/${id}`, databaseField);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/delete/${id}`);
  }

  getList(): Observable<DatabaseField[]> {
    return this.http.get<DatabaseField[]>(`${this.baseUrl}/getListDatabaseFields`);
  }
}