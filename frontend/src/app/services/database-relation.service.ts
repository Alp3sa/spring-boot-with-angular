import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DatabaseRelationService {
  private baseUrl = '/databaseRelation';

  constructor(private http: HttpClient) { }

  get(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/get/${id}`);
  }

  create(databaseRelation: Object): Observable<Object> {
    return this.http.post(`${this.baseUrl}/post`, databaseRelation);
  }

  update(id: number, databaseRelation: Object): Observable<Object> {
    return this.http.put(`${this.baseUrl}/put/${id}`, databaseRelation);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/delete/${id}`);
  }

  getList(): Observable<any> {
    return this.http.get(`${this.baseUrl}/getlist`);
  }
}