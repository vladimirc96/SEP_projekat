import { Component, OnInit } from "@angular/core";
import { FormGroup, FormControl } from "@angular/forms";
import { SellersService } from 'src/app/services/sellers.service';

@Component({
    selector: "app-new-payment-method",
    templateUrl: "./new-payment-method.component.html",
    styleUrls: ["./new-payment-method.component.css"]
})
export class NewPaymentMethodComponent implements OnInit {
    isRegistered: boolean = false;

    assignedId;

    paymentMethodForm = new FormGroup({
		serviceName: new FormControl(""),
		serviceBaseUrl: new FormControl(""),
		registrationLink: new FormControl(""),
    });

    constructor(private sellersService: SellersService) {}

    ngOnInit() {}

    onSubmit() {
		this.isRegistered = false;

        let paymentMethodDTO = {
            name: this.paymentMethodForm.value.serviceName,
			serviceBaseUrl: this.paymentMethodForm.value.serviceBaseUrl,
			registrationLink: this.paymentMethodForm.value.registrationLink
		};
		
		this.sellersService.createNewPaymentMethod(paymentMethodDTO).subscribe(
			res => {
                this.assignedId = res;
				this.isRegistered = true;
			}, err => console.log(err.error)
		)

    }

    goHome() {
        this.isRegistered = false;
    }
}
