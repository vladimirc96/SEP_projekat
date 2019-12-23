import { HttpClient } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { Router } from '@angular/router';
import { ReturnStatement } from '@angular/compiler';


@Injectable()
export class PaypalService {

    constructor(private http: HttpClient, private router: Router) { }

    pay(orderDTO) {
        // return this.http.post("/api/paypal-service/paypal", orderDTO, {responseType: 'text'});
        return this.http.post("https://localhost:8443/paypal", orderDTO, {responseType: 'text'});
    }

}