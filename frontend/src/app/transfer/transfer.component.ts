import { Component, OnInit } from '@angular/core';
import { NgFor } from '@angular/common';
import { TransferService } from './transfer.service';
import { Transfer } from './transfer.model';

@Component({
  selector: 'app-transfer',
  standalone: true,
  imports: [NgFor],
  templateUrl: './transfer.component.html',
  styleUrl: './transfer.component.css'
})
export class TransferComponent  implements OnInit {

  transfers: Transfer[] = [] ;

constructor(private transferService: TransferService) {}


  ngOnInit(): void {
    this.getTransfers();
  }
    getTransfers(): void {
      this.transferService.getTransfers()
          .subscribe(transfers => this.transfers = transfers);
    }
}

