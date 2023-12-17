import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
//import { Observable } from 'rxjs';
import { Transfer } from './transfer.model';
import {TRANSFERS} from './mock-transfers';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';


@Injectable({
  providedIn: 'root',
})
export class TransferService {
  private apiUrl = 'http://localhost:5007/api/payments';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' ,
     'Access-Control-Allow-Origin': 'http://localhost:4200' // Замените на URL вашего Angular-приложения
        })
  };

  constructor(private http: HttpClient) {}

  getTransfers(): Observable<Transfer[]> {
    return this.http.get<Transfer[]>(this.apiUrl, this.httpOptions);
  }
}

