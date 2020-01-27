import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SellersComponent } from './sellers/sellers.component';
import { PaypalComponent } from './paypal/paypal.component';
import { PaymentSuccessComponent } from './paypal/payment-success/payment-success.component';
import { PaymentCancelComponent } from './paypal/payment-cancel/payment-cancel.component';
import { LoadingPaymentComponent } from './paypal/loading-payment/loading-payment.component';
import { CentralaMockComponent } from './centrala-mock/centrala-mock.component';
import { RadComponent } from './centrala-mock/rad/rad.component';
import { SellerMethodsComponent } from './sellers/seller-methods/seller-methods.component';
import { BitcoinComponent } from './bitcoin/bitcoin.component';
import { BankComponent } from './bank/bank.component';
import { SucccessComponent } from './succcess/succcess.component';
import { CancelComponent } from './cancel/cancel.component';
import { BankPaymentComponent } from './bank/bank-payment/bank-payment.component';
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

const appRoutes: Routes = [
	{ path: '', redirectTo: '/centrala', pathMatch: 'full'},
    { path: 'home', component: SellersComponent},
	{ path: 'paypal', component: PaypalComponent},
	{ path: 'paypal/success', component: PaymentSuccessComponent},
	{ path: 'paypal/cancel', component: PaymentCancelComponent},
	{ path: 'payment/verifying', component: LoadingPaymentComponent},
	{ path: 'paypal/plan/:id', component: CreatePlanComponent},
	{ path: 'paypal/plan/subscribe', component: ShippingAdressComponent},
	{ path: 'paypal/plan/execute', component: ExecutePlanComponent},
	{ path: 'centrala', component: CentralaMockComponent},
	{ path: 'centrala/rad/:id', component: RadComponent},
	{ path: 'sellers/:id', component: SellerMethodsComponent},
	{ path: 'bitcoin', component: BitcoinComponent },
	{ path: 'bank', component: BankComponent },
	{ path: 'success', component: SucccessComponent },
	{ path: 'cancel', component: CancelComponent },

	{ path: 'bank/:id', component: BankComponent, children: [
		{ path: '', component: BankPaymentComponent },
		{ path: 'form', component: BankPaymentFormComponent },
		{ path: 'success', component: BankPaymentSuccessComponent },
		{ path: 'failure', component: BankPaymentFailureComponent },
	]
	},
	{ path: 'reg/:sellerId', component: SellerRegistrationComponent }
]

@NgModule({
	imports: [RouterModule.forRoot(appRoutes)],
	exports: [ RouterModule ]
})



export class AppRoutingModule { }