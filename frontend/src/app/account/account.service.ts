import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
//import { Observable } from 'rxjs';
import { Account } from './account.model';
import {ACCOUNTS} from './mock-accounts';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';


@Injectable({
  providedIn: 'root',
})
export class AccountService {
  private apiUrl = 'http://localhost:5003/api/accounts';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' ,
     'Access-Control-Allow-Origin': 'http://localhost:4200' // Замените на URL вашего Angular-приложения
        })
  };

  constructor(private http: HttpClient) {}

  getAccounts(): Observable<Account[]> {
    return this.http.get<Account[]>(this.apiUrl, this.httpOptions);
  }
}
