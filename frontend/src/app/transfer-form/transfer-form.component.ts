import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { TransferService } from '../transfer/transfer.service';
import { AccountService } from '../account/account.service';
import { Account } from '../account/account.model';
import {map, startWith} from 'rxjs/operators';
import {Observable, of} from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';


@Component({
  selector: 'app-transfer-form',
  templateUrl: './transfer-form.component.html'
})
export class TransferFormComponent implements OnInit  {
  createTransferForm: FormGroup;
  debitControl = new FormControl('', Validators.required);
  creditControl = new FormControl('', Validators.required);

  accountCodes: string[];

  filteredDebitAccountCodes: Observable<string[]>;
  filteredCreditAccountCodes: Observable<string[]>;

  constructor(private fb: FormBuilder, private transferService: TransferService,
   private accountService: AccountService,  private snackBar: MatSnackBar ) {
this.createTransferForm = this.fb.group({
  debitAccountCode: this.debitControl,
  creditAccountCode: this.creditControl,
  transferAmount: [
    '',
    Validators.required,
    (control: FormControl): Observable<any> => {
      const isValid = /^\d+$/.test(control.value);
      return of(isValid ? null : { invalidNumber: true });
    },
  ],
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

  this.debitControl.valueChanges.subscribe(() => {
    if (this.debitControl.value === '') {
      this.debitControl.setErrors(null);
    }
  });

  this.creditControl.valueChanges.subscribe(() => {
    if (this.creditControl.value === '') {
      this.creditControl.setErrors(null);
    }
  });

  const transferAmountControl = this.createTransferForm.get('transferAmount');
  if (transferAmountControl) {
    transferAmountControl.valueChanges.subscribe(() => {
      if (transferAmountControl.value === '') {
        transferAmountControl.setErrors(null);
      }
    });
  }
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
           this.snackBar.open('The transfer has not been completed: ' + error.error, 'Close', { duration: 8000 });

        }
      );
    }
  }
 }




