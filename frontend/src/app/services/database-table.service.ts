import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DatabaseTable } from '../models/databaseTable';

@Injectable({
  providedIn: 'root'
})
export class DatabaseTableService {
  private baseUrl = '/databaseTable';

  constructor(private http: HttpClient) { }

  get(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/get/${id}`);
  }

  create(databaseTable: Object): Observable<Object> {
    return this.http.post(`${this.baseUrl}/post`, databaseTable);
  }

  update(id: number, databaseTable: Object): Observable<Object> {
    return this.http.put(`${this.baseUrl}/put/${id}`, databaseTable);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/delete/${id}`);
  }

  getList(): Observable<DatabaseTable[]> {
    return this.http.get<DatabaseTable[]>(`${this.baseUrl}/getListDatabaseTables`);
  }
}