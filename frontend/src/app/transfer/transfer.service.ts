import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Transfer } from './transfer.model';
import {TRANSFERS} from './mock-transfers';
import { Observable, of, Subject, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';


@Injectable({
  providedIn: 'root',
})
export class TransferService {
  private apiUrl = 'http://localhost:5007/api/transfers';
  private transferCreatedSource = new Subject<void>();
 private accountUpdatedSource = new Subject<void>();

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' ,
     'Access-Control-Allow-Origin': 'http://localhost:4200'
        })
  };

  transferCreated$ = this.transferCreatedSource.asObservable();
  accountUpdated$ = this.accountUpdatedSource.asObservable();

  constructor(private http: HttpClient) {}

  getTransfers(): Observable<Transfer[]> {
    return this.http.get<Transfer[]>(this.apiUrl, this.httpOptions);
  }

createTransfer(data: any): Observable<any> {
  return this.http.post(this.apiUrl, data, { ...this.httpOptions, observe: 'response' }).pipe(
    tap(
      (response: HttpResponse<any>) => {
        if (response.status === 201) {
          this.transferCreatedSource.next();
          this.accountUpdatedSource.next();
        }
      },
      (error) => {
        console.error('Error during transfer creation:', error);
      }
    ),
    catchError((error) => {
      if (error.status !== 201) {
        console.error('Error response from server:', error);
        return throwError(error);
      } else {
        return of();
      }
    })
  );
}

}

