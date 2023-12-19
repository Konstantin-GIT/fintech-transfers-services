import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { TransferService } from '../transfer/transfer.service';
import { AccountService } from '../account/account.service';
import { Account } from '../account/account.model';
import {map, startWith} from 'rxjs/operators';
import {Observable} from 'rxjs';


@Component({
  selector: 'app-transfer-form',
  templateUrl: './transfer-form.component.html'
})
export class TransferFormComponent implements OnInit  {
  createTransferForm: FormGroup;
  debitControl = new FormControl('');
  creditControl = new FormControl('');

  accountCodes: string[];

  filteredDebitAccountCodes: Observable<string[]>;
  filteredCreditAccountCodes: Observable<string[]>;

  constructor(private fb: FormBuilder, private transferService: TransferService, private accountService: AccountService) {
    this.createTransferForm = this.fb.group({
      debitAccountCode: ['', Validators.required],
      creditAccountCode: ['', Validators.required],
      transferAmount: ['', Validators.required],

    });
    this.accountCodes = [];
    this.filteredDebitAccountCodes = new Observable<string[]>();
    this.filteredCreditAccountCodes = new Observable<string[]>();
  }

  ngOnInit() {
    this.accountService.accounts.subscribe((accounts) => {
      this.accountCodes = accounts.map(account => account.code);

      this.filteredDebitAccountCodes = this.debitControl.valueChanges.pipe(
        startWith(''),
        map(value => this._filter(value || '')),
      );

      this.filteredCreditAccountCodes = this.creditControl.valueChanges.pipe(
        startWith(''),
        map(value => this._filter(value || '')),
      );


    });
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();
    console.log(this.accountCodes);
    return this.accountCodes.filter(code => code.toLowerCase().includes(filterValue));
  }

  onSubmit() {
    if (this.createTransferForm.valid) {
      const formData = this.createTransferForm.value;

      this.transferService.createTransfer(formData).subscribe(
        (response) => {
          console.log('Success:', response);
        },
        (error) => {
          console.error('Error:', error);
        }
      );
    }
  }
 }




