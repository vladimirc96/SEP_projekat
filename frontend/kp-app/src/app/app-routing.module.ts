import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SellersComponent } from './sellers/sellers.component';
import { PaypalComponent } from './paypal/paypal.component';
import { PaymentSuccessComponent } from './paypal/payment-success/payment-success.component';
import { PaymentCancelComponent } from './paypal/payment-cancel/payment-cancel.component';
import { LoadingPaymentComponent } from './paypal/loading-payment/loading-payment.component';
import { CentralaMockComponent } from './centrala-mock/centrala-mock.component';
import { RadComponent } from './centrala-mock/rad/rad.component';

const appRoutes: Routes = [
	{ path: '', redirectTo: '/home', pathMatch: 'full'},
    { path: 'home', component: SellersComponent},
	{ path: 'paypal', component: PaypalComponent},
	{ path: 'paypal/success', component: PaymentSuccessComponent},
	{ path: 'paypal/cancel', component: PaymentCancelComponent},
	{ path: 'payment/verifying', component: LoadingPaymentComponent},
	{ path: 'centrala', component: CentralaMockComponent},
	{ path: 'centrala/rad/:id', component: RadComponent},

]

@NgModule({
	imports: [RouterModule.forRoot(appRoutes)],
	exports: [ RouterModule ]
})



export class AppRoutingModule { }