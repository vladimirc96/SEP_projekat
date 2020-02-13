import { Injectable } from "@angular/core";
import { PmComponent } from '../model/pm-component.model';
import { BankRegistrationComponent } from '../registrations/bank-registration/bank-registration.component';
import { PaypalRegistrationComponent } from '../registrations/paypal-registration/paypal-registration.component';
import { BitcoinRegistrationComponent } from '../registrations/bitcoin-registration/bitcoin-registration.component';

@Injectable({
    providedIn: "root"
})
export class PmService {

	public pmRegistrationComponents: PmComponent[] = [
        new PmComponent(BankRegistrationComponent, 1),
        new PmComponent(PaypalRegistrationComponent, 2),
        new PmComponent(BitcoinRegistrationComponent, 3)
    ];
	
	constructor() {
	}
	

}
