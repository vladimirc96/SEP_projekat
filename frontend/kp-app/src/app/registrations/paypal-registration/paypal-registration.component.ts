import { Component, OnInit, Input } from "@angular/core";
import { SellersService } from 'src/app/services/sellers.service';

@Component({
    selector: "app-paypal-registration",
    templateUrl: "./paypal-registration.component.html",
    styleUrls: ["./paypal-registration.component.css"]
})
export class PaypalRegistrationComponent implements OnInit {
   
	
    @Input() sellerId: any;
    @Input() registrationLink: any;
    
    constructor(private sellersService: SellersService) {

    }

    ngOnInit() {
        console.log(this.sellerId);
    
    }
}
