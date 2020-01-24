import { Component, OnInit, Input } from "@angular/core";
import { SellersService } from 'src/app/services/sellers.service';

@Component({
    selector: "app-bank-registration",
    templateUrl: "./bank-registration.component.html",
    styleUrls: ["./bank-registration.component.css"]
})
export class BankRegistrationComponent implements OnInit {

    
    @Input() sellerId: any;
    @Input() registrationLink: any;

    constructor(private sellersService: SellersService) {}

    ngOnInit() {
        console.log(this.sellerId);
    }
}
