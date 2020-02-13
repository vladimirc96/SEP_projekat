import { HttpClient } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { Router } from '@angular/router';
import { ReturnStatement } from '@angular/compiler';


@Injectable()
export class PaypalService {

    constructor(private http: HttpClient, private router: Router) { }

    pay(orderDTO) {
        return this.http.post("api/paypal-service/paypal", orderDTO, {responseType: 'text'});
        // return this.http.post("http://localhost:8443/paypal", orderDTO, {responseType: 'text'});
    }

    checkStatus(payment) {
        return this.http.get("api/paypal-service/paypal/status/".concat(payment), {responseType: 'text'});
        // return this.http.get("http://localhost:8443/paypal/status/".concat(payment), {responseType: 'text'});
    }

    completePayment(paymentID, payerID) {
        return this.http.get("api/paypal-service/paypal/success/"+ paymentID + "/" + payerID, {responseType: 'text'});
        // return this.http.get("http://localhost:8443/paypal/success/"+ paymentID + "/" + payerID, {responseType: 'text'});
    }

    createPlan(planDTO) {
        return this.http.post("api/paypal-service/paypal/plan", planDTO, {responseType: 'text'});
        // return this.http.post("http://localhost:8443/paypal/plan", planDTO, {responseType: 'text'});
    }

    createAgreement(dto, username) {
        return this.http.post("api/paypal-service/paypal/plan/agreement/" + username, dto, {responseType: 'text'});
        // return this.http.post("http://localhost:8443/paypal/plan/agreement", dto, {responseType: 'text'});
    }

    executePlan(token) {
        return this.http.get("api/paypal-service/paypal/plan/execute/".concat(token), {responseType: 'text'});
        // return this.http.get("http://localhost:8443/paypal/plan/execute/".concat(token), {responseType: 'text'});
    }

    getPaypalSubscriptionPlans(id) {
        return this.http.get("api/paypal-service/paypal/getSpecificPlans/".concat(id));
    }

    cancelPayment(token) {
        return this.http.get("api/paypal-service/paypal/cancelPayment/".concat(token), {responseType: 'text'});
    }

    cancelSubscription(token) {
        return this.http.get("api/paypal-service/paypal/cancelSubscription/".concat(token), {responseType: 'text'});
    }

    getAllPlans(selId) {
        return this.http.get("api/paypal-service/paypal/getAllPlans/".concat(selId));
    }

    cancelBillingPlan(planID, sellerID) {
        return this.http.put("api/paypal-service/paypal/cancelBillingPlan/".concat(planID).concat("/").concat(sellerID), null, {responseType: 'text'});
    }

}