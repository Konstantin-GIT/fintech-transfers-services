import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { provideHttpClient } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AccountComponent } from './account/account.component';
import { TransferComponent } from './transfer/transfer.component';
import { TransferFormComponent } from './transfer-form/transfer-form.component';
import { BrowserModule } from '@angular/platform-browser';

@NgModule({
  declarations: [
    AppComponent,
    AccountComponent,
    TransferComponent,
    TransferFormComponent
  ],
  providers: [provideHttpClient()
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

