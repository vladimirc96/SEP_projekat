import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { PaypalService } from '../services/paypal.service';
import { ActiveOrderService } from '../services/active-order.service';
import { SellersService } from '../services/sellers.service';

@Component({
  selector: 'app-subscription-plan',
  templateUrl: './subscription-plan.component.html',
  styleUrls: ['./subscription-plan.component.css']
})
export class SubscriptionPlanComponent implements OnInit {

  id: any;
  NC: string = "http://localhost:4201";
  ppizbor: any;
  PayPalPlans: any;

  imaPPPlanova: boolean = false;
  seller: any = null;
	activeOrder: any = null;

  constructor(private activeOrderService: ActiveOrderService, private sellerService: SellersService, private paypalService: PaypalService, private route: ActivatedRoute, private router: Router) {
    this.route.params.subscribe((params: Params) => {
			const param = +params["id"];

			if (!isNaN(param)) {
        this.id = param;
        this.getActiveOrder();
			} else {
				window.location.href = this.NC;
			}
		});
  }

  ngOnInit() {
  }

  getActiveOrder(){
		this.activeOrderService.getActiveOrder(this.id).subscribe(
			(success) => {
        this.activeOrder = success;
        this.getPayPalSubscriptions(this.activeOrder.sellerId);
			},
			error => alert(error.error),
		);
  }

  getPayPalSubscriptions(id) {
    this.paypalService.getPaypalSubscriptionPlans(id).subscribe(
      (response) => {
        this.PayPalPlans = response;
        if(this.PayPalPlans !== undefined && this.PayPalPlans.length != 0) {
          this.ppizbor = this.PayPalPlans[0].id;
          this.imaPPPlanova = true;
        }
      },
      (error) => {
        alert(error.message);
      }
    );
  }

  onPPSub() {
    localStorage.setItem("plan", this.ppizbor);
    window.location.href = "https://localhost:4200/paypal/plan/subscribe/".concat(this.ppizbor).concat("/").concat(this.activeOrder.sellerId);
  }

}
