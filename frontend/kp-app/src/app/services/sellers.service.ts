import { HttpClient } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { Router } from '@angular/router';
import { ReturnStatement } from '@angular/compiler';


@Injectable()
export class SellersService {

    private ENDPOINT_URI: string = "api/sellers";

    public paymentMethods = [
        {
            id: 1,
            name: "Credit/Debit card"
        },
        {
            id: 2,
            name: "PayPal"
        },
        {
            id: 3,
            name: "Bitcoin"
        }
    ]

    constructor(private http: HttpClient, private router: Router) { }

    getSeller(id: number) { 
        return this.http.get(this.ENDPOINT_URI + "/sellers/" + id);
    }

    initRegister() {
        return this.http.get(this.ENDPOINT_URI + "/sellers/register");
    }

    register(dto) {
        return this.http.post(this.ENDPOINT_URI + "/sellers/register", dto);
    }

    getActivePlan(id) {
        return this.http.get(this.ENDPOINT_URI.concat("/sellers/getActivePlan/").concat(id));
    }

    removeActivePlan(id) {
        return this.http.get(this.ENDPOINT_URI.concat("/sellers/removeActivePlan/").concat(id), {responseType: 'text'});
    }

}