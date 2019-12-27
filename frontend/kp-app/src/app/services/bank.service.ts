import { HttpClient } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { Router } from '@angular/router';
import { ReturnStatement } from '@angular/compiler';


@Injectable()
export class BankService {

    constructor(private http: HttpClient, private router: Router) { }


    paymentRequest(paymentDTO){
        return this.http.post("/api/bank-service/bank/payment-request", paymentDTO);
    }

    validate(bankAccountDTO, transactionId){
        return this.http.put("https://localhost:8450/bank/validate/" + transactionId, bankAccountDTO);
    }

    payment(bankAccountDTO, transactionId){
        return this.http.put("https://localhost:8450/bank/payment/" + transactionId, bankAccountDTO);
    }


}