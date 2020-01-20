import { HttpClient } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { Router } from '@angular/router';
import { ReturnStatement } from '@angular/compiler';


@Injectable()
export class PaypalService {

    constructor(private http: HttpClient, private router: Router) { }

    pay(orderDTO) {
        // return this.http.post("api/paypal-service/paypal", orderDTO, {responseType: 'text'});
        return this.http.post("http://localhost:8443/paypal", orderDTO, {responseType: 'text'});
    }

    checkStatus(payment) {
        // return this.http.get("api/paypal-service/paypal/status/".concat(payment), {responseType: 'text'});
        return this.http.get("http://localhost:8443/paypal/status/".concat(payment), {responseType: 'text'});
    }

    completePayment(paymentID, payerID) {
        // return this.http.get("api/paypal-service/paypal/success/"+ paymentID + "/" + payerID, {responseType: 'text'});
        return this.http.get("http://localhost:8443/paypal/success/"+ paymentID + "/" + payerID, {responseType: 'text'});
    }

}