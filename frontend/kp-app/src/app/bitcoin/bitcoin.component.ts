import { Component, OnInit } from "@angular/core";
import { CentralaService } from "../services/centrala.service";
import { BitcoinService } from '../services/bitcoin.service';
import { Router } from '@angular/router';

@Component({
  selector: "app-bitcoin",
  templateUrl: "./bitcoin.component.html",
  styleUrls: ["./bitcoin.component.css"]
})
export class BitcoinComponent implements OnInit {
	rad: any = null;
	btcPrice = null;
	desc: string = "";

	transaction: any = null;



	loadingMessage: boolean = false;
	redirectMessage: boolean = false;
	errorMessage = null;

	constructor(private centralaService: CentralaService, private bitcoinService: BitcoinService, private router: Router) {
		this.rad = this.centralaService.activeRad;
		this.convertPrice();
	}

	ngOnInit() {}

	convertPrice() {
		this.bitcoinService.getRate("USD", "BTC").subscribe(
			(success:any) => this.btcPrice = success.rate * this.rad.price,
			error => console.log(error)
		)
	}

	onContinue() {

		const dto = {
			sellerId: this.rad.sellerId,
			currency: "BTC",
			amount: this.btcPrice,
			title: "Payment for: " + this.rad.title,
			description: this.desc
		}

		this.bitcoinService.createPayment(dto).subscribe(
			(success:any) => {
				this.loadingMessage = true;
				this.transaction = success;
				console.log(this.transaction);
				window.open(success.paymentUrl, "_blank");
				
				this.setRefreshInterval();

			}, error => console.log(error)
		);
	}

	setRefreshInterval() {
		this.transaction.interval = setInterval(() => {
			this.checkTransactionStatus();
		}, 10000);
	}

	checkTransactionStatus() {
		this.bitcoinService.getTransactionStatus(this.transaction.id).subscribe(
			(success:any) => {
				this.transaction.status = success.status;
				if (this.transaction.status == "paid") {
					clearInterval(this.transaction.interval);
					this.loadingMessage = false;
					this.redirectMessage = true;
					setInterval(() => {
						this.router.navigate(['/success']);
					}, 5000)
				} else if (this.transaction.status == "invalid" || this.transaction.status == "experexpiredied" || this.transaction.status == "canceled") {
					
					clearInterval(this.transaction.interval);
					this.loadingMessage = false;
					this.errorMessage = "Transakcija nije uspešna!"
					setInterval(() => {
						this.router.navigate(['/cancel']);
					}, 5000)
				}
			}
		)

	}
}
