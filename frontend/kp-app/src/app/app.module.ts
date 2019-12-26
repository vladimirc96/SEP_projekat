import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { SellersComponent } from './sellers/sellers.component';
import { AppRoutingModule } from './app-routing.module';
import { SellersService } from './services/sellers.service';
import { PaypalService } from './services/paypal.service';
import { PaypalComponent } from './paypal/paypal.component';
import { HttpClientModule } from '@angular/common/http';
import { PaymentSuccessComponent } from './paypal/payment-success/payment-success.component';
import { PaymentCancelComponent } from './paypal/payment-cancel/payment-cancel.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoadingPaymentComponent } from './paypal/loading-payment/loading-payment.component';
import { CentralaMockComponent } from './centrala-mock/centrala-mock.component';
import { RadComponent } from './centrala-mock/rad/rad.component';
import { SellerMethodsComponent } from './sellers/seller-methods/seller-methods.component';
import { BitcoinComponent } from './bitcoin/bitcoin.component';
import { BankComponent } from './bank/bank.component';
import { BankService } from './services/bank.service';

@NgModule({
  declarations: [
    AppComponent,
    SellersComponent,
    PaypalComponent,
    PaymentSuccessComponent,
    PaymentCancelComponent,
    LoadingPaymentComponent,
    CentralaMockComponent,
    RadComponent,
    SellerMethodsComponent,
    BitcoinComponent,
    BankComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [SellersService, PaypalService, BankService],
  bootstrap: [AppComponent]
})
export class AppModule { }
