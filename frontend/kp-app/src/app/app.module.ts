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

@NgModule({
  declarations: [
    AppComponent,
    SellersComponent,
    PaypalComponent,
    PaymentSuccessComponent,
    PaymentCancelComponent,
    LoadingPaymentComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [SellersService, PaypalService],
  bootstrap: [AppComponent]
})
export class AppModule { }
