import { Component, OnInit } from "@angular/core";
import { CentralaService } from "../services/centrala.service";
import { BitcoinService } from '../services/bitcoin.service';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { SellersService } from '../services/sellers.service';
import { ActiveOrderService } from '../services/active-order.service';

@Component({
  selector: "app-bitcoin",
  templateUrl: "./bitcoin.component.html",
  styleUrls: ["./bitcoin.component.css"]
})
export class BitcoinComponent implements OnInit {
	

	activeOrder: any = null;
	activeOrderId; 
	btcPrice = null;
	desc: string = "";
	math = Math;

	transaction: any = null;



	loadingMessage: boolean = false;
	redirectMessage: boolean = false;
	errorMessage = null;

	constructor(private aoService: ActiveOrderService, private bitcoinService: BitcoinService, private router: Router, private route: ActivatedRoute) {
		
		this.route.params.subscribe((params: Params) => {
			
			console.log(params);
			const param = +params["id"];

			console.log(param);

			if (!isNaN(param)) {
				this.activeOrderId = param;
				console.log("HERE!!!!!!!!");
				this.fetchActiveOrder();
			} else {
				this.router.navigate(["/"]);
			}
		});
	}

	ngOnInit() {}

	fetchActiveOrder() {
		this.aoService.getActiveOrder(this.activeOrderId).subscribe(
			(res: any) => {
				if (res.orderStatus !== "CREATED") {
					this.orderProcessedByAnotherServiceError();
				}
				this.activeOrder = res;
				this.convertPrice();
			}, err => console.log(err.error)
		)
	}

	convertPrice() {
		this.bitcoinService.getRate("USD", "BTC").subscribe(
			(success:any) => this.btcPrice = success.rate * this.activeOrder.amount,
			error => console.log(error)
		)
	}

	onContinue() {

		const dto = {
			activeOrderId: this.activeOrder.id,
			sellerId: this.activeOrder.sellerId,
			currency: "BTC",
			amount: this.btcPrice,
			title: "Payment for: " + this.activeOrder.title,
			description: this.desc
		}

		this.bitcoinService.createPayment(dto).subscribe(
			(success:any) => {
				this.loadingMessage = true;
				this.transaction = success;
				console.log(this.transaction);
				window.open(success.paymentUrl, "_blank");
				
				this.setRefreshInterval();

			}, error => {
				if (error.status == 409) {
					this.orderProcessedByAnotherServiceError();
				}
			}
		);
	}

	setRefreshInterval() {
		this.transaction.interval = setInterval(() => {
			this.checkTransactionStatus();
		}, 6000);
	}

	orderProcessedByAnotherServiceError() {
		alert('Order is beeing processed by another service.');
		this.goHome();
	}

	goHome() {
		window.location.href = "http://localhost:4201/";
	}
	

	checkTransactionStatus() {
		this.bitcoinService.getTransactionStatus(this.transaction.id).subscribe(
			(success:any) => {
				this.transaction.status = success.status;
				this.transaction.amountDifference = success.amountDifference;
				if (this.transaction.status == "paid") {
					clearInterval(this.transaction.interval);
					this.loadingMessage = false;
					this.redirectMessage = true;
					setTimeout(() => {
						this.router.navigate(['/success']);
					}, 3000)
				} else if (this.transaction.status == "invalid" || this.transaction.status == "canceled") {
					clearInterval(this.transaction.interval);
					this.loadingMessage = false;
					this.errorMessage = "Transakcija nije uspešna!"
				} else if (this.transaction.status == "expired") {
					clearInterval(this.transaction.interval);
					this.loadingMessage = false;
					this.errorMessage = "Transakcija nije uspešna! Predviđeno vreme za uplatu je isteklo."
					
				}
			}
		)

	}
}
