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

  accounts = new Observable<Account[]>;

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' ,
     'Access-Control-Allow-Origin': 'http://localhost:4200' // Замените на URL вашего Angular-приложения
        })
  };

  constructor(private http: HttpClient) {}

  getAccounts(): Observable<Account[]> {
    this.accounts = this.http.get<Account[]>(this.apiUrl, this.httpOptions);
    return this.accounts;
  }
}
