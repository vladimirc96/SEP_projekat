import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router, Params } from '@angular/router';
import { SellersService } from 'src/app/services/sellers.service';

@Component({
  selector: "app-seller-methods",
  templateUrl: "./seller-methods.component.html",
  styleUrls: ["./seller-methods.component.css"]
})
export class SellerMethodsComponent implements OnInit {

	id: number;
	seller: any = null;

	constructor(private route: ActivatedRoute, private router: Router, private sellerService: SellersService) {
		this.route.params.subscribe((params: Params) => {
			const param = +params["id"];

			if (!isNaN(param)) {
				this.id = param;
				this.fetchSeller();
			} else {
				this.router.navigate(["/"]);
			}
		});
	}

	ngOnInit() {

	}

	fetchSeller() {
		this.sellerService.getSeller(this.id).subscribe(
			success => this.seller = success,
			error => alert(error.error)
		)
	}

	onClickPM(pm) {
		switch (pm) {
			case "Credit/Debit Card":
				this.router.navigate(["/bank"]);
				break;
			case "PayPal":
				this.router.navigate(["/paypal"]);
				break;
			case "Bitcoin":
				this.router.navigate(["/bitcoin"]);
				break;
		
			default:
				break;
		}
	}
}
