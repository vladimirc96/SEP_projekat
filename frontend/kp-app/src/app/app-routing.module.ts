import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SellersComponent } from './sellers/sellers.component';
import { PaypalComponent } from './paypal/paypal.component';
import { PaymentSuccessComponent } from './paypal/payment-success/payment-success.component';
import { PaymentCancelComponent } from './paypal/payment-cancel/payment-cancel.component';
import { LoadingPaymentComponent } from './paypal/loading-payment/loading-payment.component';

const appRoutes: Routes = [
	{ path: '', redirectTo: '/home', pathMatch: 'full'},
    { path: 'home', component: SellersComponent},
	{ path: 'paypal', component: PaypalComponent},
	{ path: 'paypal/success', component: PaymentSuccessComponent},
	{ path: 'paypal/cancel', component: PaymentCancelComponent},
	{ path: 'payment/verifying', component: LoadingPaymentComponent}

]

@NgModule({
	imports: [RouterModule.forRoot(appRoutes)],
	exports: [ RouterModule ]
})



export class AppRoutingModule { }