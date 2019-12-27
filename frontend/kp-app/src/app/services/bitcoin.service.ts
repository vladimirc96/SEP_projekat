import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: "root"
})
export class BitcoinService {

	ENDPOINT_URL: string = "api/bitcoin-service/payment";
	constructor(private http: HttpClient) {}

	getRate(from: string, to: string) {
		return this.http.get(this.ENDPOINT_URL + "/rate/" + from + "/" + to);
	}

	createPayment(order) {
		return this.http.post(this.ENDPOINT_URL + "/", order);
	}

	getTransaction(id) {
		return this.http.get(this.ENDPOINT_URL + "/" + id);
	}

	getTransactionStatus(id) {
		return this.http.get(this.ENDPOINT_URL + "/" + id + "/status");
	}
}
