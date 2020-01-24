import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { SellersService } from 'src/app/services/sellers.service';
import { Validators, FormBuilder } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
    selector: "app-paypal-registration",
    templateUrl: "./paypal-registration.component.html",
    styleUrls: ["./paypal-registration.component.css"]
})
export class PaypalRegistrationComponent implements OnInit {
   
	
    @Input() sellerId: any;
    @Input() registrationLink: any;

    @Output() output: EventEmitter<boolean> = new EventEmitter();

    payPalRegistrationForm = this.fb.group({
        clientId: ["", Validators.required],
        clientSecret: ["", Validators.required],
        password: ["", Validators.required],
	  });

    constructor(private sellersService: SellersService, private fb: FormBuilder, private http: HttpClient) {

    }

    ngOnInit() {
        console.log(this.sellerId);
    }

    onSubmit() {
        let dto = {
            id: this.sellerId,
            password: this.payPalRegistrationForm.get('password').value,
            clientId: this.payPalRegistrationForm.get('clientId').value,
            clientSecret: this.payPalRegistrationForm.get('clientSecret').value,
        }

        this.registerPayPal(dto);
    }

    registerPayPal(dto: any) {

        this.http.post(this.registrationLink, dto).subscribe(
            (res: any) => {
                this.output.emit(true);
            }, error => console.log(error.error)
        )
    }
}
