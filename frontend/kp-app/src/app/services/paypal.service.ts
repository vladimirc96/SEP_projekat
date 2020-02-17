import { HttpClient } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { Router } from '@angular/router';
import { ReturnStatement } from '@angular/compiler';


@Injectable()
export class PaypalService {

    constructor(private http: HttpClient, private router: Router) { }

    pay(orderDTO) {
        return this.http.post("api/paypal-service/paypal", orderDTO, {responseType: 'text'});
    }

    checkStatus(payment) {
        return this.http.get("api/paypal-service/paypal/status/".concat(payment));
    }

    completePayment(paymentID, payerID) {
        return this.http.get("api/paypal-service/paypal/success/"+ paymentID + "/" + payerID, {responseType: 'text'});
    }

    createPlan(planDTO) {
        return this.http.post("api/paypal-service/paypal/plan", planDTO, {responseType: 'text'});
    }

    createAgreement(dto, username) {
        return this.http.post("api/paypal-service/paypal/plan/agreement/" + username, dto, {responseType: 'text'});
    }

    executePlan(token) {
        return this.http.get("api/paypal-service/paypal/plan/execute/".concat(token), {responseType: 'text'});
    }

    getPaypalSubscriptionPlans(id) {
        return this.http.get("api/paypal-service/paypal/getSpecificPlans/".concat(id));
    }

    cancelPayment(token) {
        return this.http.get("api/paypal-service/paypal/cancelPayment/".concat(token));
    }

    cancelSubscription(token) {
        return this.http.get("api/paypal-service/paypal/cancelSubscription/".concat(token));
    }

    getAllPlans(selId) {
        return this.http.get("api/paypal-service/paypal/getAllPlans/".concat(selId));
    }

    cancelBillingPlan(planID, sellerID) {
        return this.http.put("api/paypal-service/paypal/cancelBillingPlan/".concat(planID).concat("/").concat(sellerID), null, {responseType: 'text'});
    }

}