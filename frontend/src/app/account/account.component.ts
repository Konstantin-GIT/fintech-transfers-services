import { Component, OnInit } from '@angular/core';
import { NgFor } from '@angular/common';
import { AccountService } from './account.service';
import { Account } from './account.model';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class AccountComponent  implements OnInit {

  accounts: Account[] = [] ;

constructor(private accountService: AccountService) {}


  ngOnInit(): void {
    this.getAccounts();
  }
    getAccounts(): void {
      this.accountService.getAccounts()
          .subscribe(accounts => this.accounts = accounts);
    }
}

