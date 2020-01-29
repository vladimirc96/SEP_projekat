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

    validateAndReserve(bankAccountDTO, transactionId){
        return this.http.put("https://localhost:8450/bank/acquirer/payment/" + transactionId, bankAccountDTO);
    }

    confirmPaymentAcquirer(bankAccountDTO, transactionId){
        return this.http.put("https://localhost:8450/bank/confirm-payment-acquirer/" + transactionId, bankAccountDTO);
    }

    confirmPaymentIssuer(bankAccountDTO, transactionId){
        return this.http.put("https://localhost:8450/bank/confirm-payment-issuer/" + transactionId, bankAccountDTO);
    }

    


}