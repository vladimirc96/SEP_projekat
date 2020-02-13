import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { Params, ActivatedRoute } from '@angular/router';
import { SellersService } from 'src/app/services/sellers.service';
import { FormBuilder, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { IRegistrationComponent } from 'src/app/interfaces/i-registration.component';

@Component({
    selector: "app-bitcoin-registration",
    templateUrl: "./bitcoin-registration.component.html",
    styleUrls: ["./bitcoin-registration.component.css"]
})
export class BitcoinRegistrationComponent implements OnInit, IRegistrationComponent {

    @Input() sellerId: any;
    @Input() registrationLink: any;

    @Output() output: EventEmitter<boolean> = new EventEmitter();

    btcRegistrationForm = this.fb.group({
		password: ["", Validators.required],
		authToken: ["", Validators.required]
	  });
    
    constructor(private sellersService: SellersService, private fb: FormBuilder, private http: HttpClient) {

    }

    ngOnInit() {
    
    }

    onSubmit() {
        let dto = {
            id: this.sellerId,
            password: this.btcRegistrationForm.get('password').value,
            authToken: this.btcRegistrationForm.get('authToken').value
        }

        this.registerBitcoin(dto);
    }

    registerBitcoin(dto: any) {

        this.http.post(this.registrationLink, dto).subscribe(
            (res: any) => {
                this.output.emit(true);
            }, error => console.log(error.error)
        )
    }
}
