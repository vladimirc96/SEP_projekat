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
import { AngularFontAwesomeModule } from 'angular-font-awesome';
import { SucccessComponent } from './succcess/succcess.component';
import { CancelComponent } from './cancel/cancel.component';

import { BankPaymentComponent } from './bank/bank-payment/bank-payment.component';
import { NgxSpinnerModule } from "ngx-spinner";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BankPaymentSuccessComponent } from './bank/bank-payment-success/bank-payment-success.component';
import { BankPaymentFailureComponent } from './bank/bank-payment-failure/bank-payment-failure.component';
import { BankPaymentFormComponent } from './bank/bank-payment-form/bank-payment-form.component';
import { CreatePlanComponent } from './paypal/billingPlans/create-plan/create-plan.component';
import { ShippingAdressComponent } from './paypal/billingPlans/shipping-adress/shipping-adress.component';
import { ExecutePlanComponent } from './paypal/billingPlans/execute-plan/execute-plan.component';
import { BitcoinRegistrationComponent } from './registrations/bitcoin-registration/bitcoin-registration.component';
import { PaypalRegistrationComponent } from './registrations/paypal-registration/paypal-registration.component';
import { BankRegistrationComponent } from './registrations/bank-registration/bank-registration.component';
import { SellerRegistrationComponent } from './registrations/seller-registration/seller-registration.component';
import { SubscriptionPlanComponent } from './subscription-plan/subscription-plan.component';
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
    BankComponent,
    SucccessComponent,
    CancelComponent,
    BankPaymentComponent,
    BankPaymentSuccessComponent,
    BankPaymentFailureComponent,
    BankPaymentFormComponent,
    CreatePlanComponent,
    ShippingAdressComponent,
    ExecutePlanComponent,
    BitcoinRegistrationComponent,
    PaypalRegistrationComponent,
    BankRegistrationComponent,
    SellerRegistrationComponent,
    SubscriptionPlanComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AngularFontAwesomeModule,
    NgxSpinnerModule,
    BrowserAnimationsModule
  ],
  providers: [SellersService, PaypalService, BankService],
  bootstrap: [AppComponent]
})
export class AppModule { }
