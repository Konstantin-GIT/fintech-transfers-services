import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { TransferService } from '../transfer/transfer.service';

@Component({
  selector: 'app-transfer-form',
  templateUrl: './transfer-form.component.html'
})
export class TransferFormComponent {
  createTransferForm: FormGroup;

  constructor(private fb: FormBuilder, private transferService: TransferService) {
    this.createTransferForm = this.fb.group({
      debitAccountCode: ['', Validators.required],
      creditAccountCode: ['', Validators.required],
      transferAmount: ['', Validators.required],
    });
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


