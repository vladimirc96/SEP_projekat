import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { SellersService } from 'src/app/services/sellers.service';
import { FormBuilder, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
    selector: "app-bank-registration",
    templateUrl: "./bank-registration.component.html",
    styleUrls: ["./bank-registration.component.css"]
})
export class BankRegistrationComponent implements OnInit {

    
    @Input() sellerId: any;
    @Input() registrationLink: any;

    @Output() output: EventEmitter<boolean> = new EventEmitter();

    bankRegistrationForm = this.fb.group({
        merchantId: ["", Validators.required],
        merchantPassword: ["", Validators.required],
        name: ["", Validators.required],
        password: ["", Validators.required],
	  });

    constructor(private sellersService: SellersService, private fb: FormBuilder, private http: HttpClient) {}

    ngOnInit() {
        console.log(this.sellerId);
    }

    onSubmit() {
        let dto = {
            id: this.sellerId,
            password: this.bankRegistrationForm.get('password').value,
            merchantId: this.bankRegistrationForm.get('merchantId').value,
            merchantPassword: this.bankRegistrationForm.get('merchantPassword').value,
            name: this.bankRegistrationForm.get('name').value
        }

        this.registerBank(dto);
    }

    registerBank(dto: any) {

        this.http.post(this.registrationLink, dto).subscribe(
            (res: any) => {
                this.output.emit(true);
            }, error => console.log(error.error)
        )
    }
}
