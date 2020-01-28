import { Component, OnInit, ANALYZE_FOR_ENTRY_COMPONENTS } from "@angular/core";
import { ActivatedRoute, Router, Params } from '@angular/router';
import { SellersService } from 'src/app/services/sellers.service';
import { CentralaService } from 'src/app/services/centrala.service';
import { ActiveOrderService } from 'src/app/services/active-order.service';

@Component({
  selector: "app-seller-methods",
  templateUrl: "./seller-methods.component.html",
  styleUrls: ["./seller-methods.component.css"]
})
export class SellerMethodsComponent implements OnInit {

	id: number;
	seller: any = null;
	activeOrder: any = null;

	constructor(private activeOrderService: ActiveOrderService,private route: ActivatedRoute, private router: Router, private sellerService: SellersService, private centralaService: CentralaService) {
		this.route.params.subscribe((params: Params) => {
			const param = +params["id"];

			if (!isNaN(param)) {
				this.id = param;
				this.getActiveOrder();
			} else {
				this.router.navigate(["/"]);
			}
		});
	}

	ngOnInit() {
	}

	fetchSeller(id) {
		this.sellerService.getSeller(id).subscribe(
			success => this.seller = success,
			error => alert(error.error)
		)
	}

	getActiveOrder(){
		this.activeOrderService.getActiveOrder(this.id).subscribe(
			(success) => {
				this.activeOrder = success;
				this.fetchSeller(this.activeOrder.sellerId);
			},
			error => alert(error.error),
		);
	}

	onClickPM(pm) {
		switch (pm) {
			case "Credit/Debit Card":
				this.router.navigate(["/bank/" + this.id]);
				break;
			case "PayPal":
				this.router.navigate(["/paypal"]);
				break;
			case "Bitcoin":
				console.log(this.id);
				this.router.navigate(["/bitcoin/" + this.id]);
				break;
		
			default:
				break;
		}
	}
}
