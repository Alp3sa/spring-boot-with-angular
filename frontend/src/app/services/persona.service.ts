import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PersonaService {

  private baseUrl = '/people';

  constructor(private http: HttpClient) { }

  getPersona(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/get/${id}`);
  }

  createPersona(persona: Object): Observable<Object> {
    return this.http.post(`${this.baseUrl}/post`, persona);
  }

  updatePersona(id: number, persona: Object): Observable<Object> {
    return this.http.put(`${this.baseUrl}/put/${id}`, persona);
  }

  deletePersona(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/delete/${id}`);
  }

  getPersonasList(): Observable<any> {
    return this.http.get(`${this.baseUrl}/getlist`);
  }

  getList(): Observable<any> {
    return this.http.get(`${this.baseUrl}/getlistDatabases`);
  }
}