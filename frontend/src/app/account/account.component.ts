import { Component, OnInit } from '@angular/core';
import { NgFor } from '@angular/common';
import { AccountService } from './account.service';
import { Account } from './account.model';
import { TransferService } from '../transfer/transfer.service';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class AccountComponent  implements OnInit {

  accounts: Account[] = [] ;

 displayedColumns: string[] = [ 'code', 'balance'];

constructor(private accountService: AccountService, private transferService: TransferService) {}


ngOnInit(): void {
  this.getAccounts();

  this.transferService.accountUpdated$.subscribe(() => {
    this.getAccounts();
  });
}
    getAccounts(): void {
      this.accountService.getAccounts()
          .subscribe(accounts => this.accounts = accounts);
    }
}

