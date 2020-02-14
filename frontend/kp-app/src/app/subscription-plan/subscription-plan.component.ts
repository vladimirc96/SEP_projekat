import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Params, Router } from "@angular/router";
import { PaypalService } from "../services/paypal.service";
import { ActiveOrderService } from "../services/active-order.service";
import { SellersService } from "../services/sellers.service";
import Swal from 'sweetalert2';

@Component({
    selector: "app-subscription-plan",
    templateUrl: "./subscription-plan.component.html",
    styleUrls: ["./subscription-plan.component.css"]
})
export class SubscriptionPlanComponent implements OnInit {
    id: any;
    websiteURL: string;
    ppizbor: any;
    PayPalPlans: any;
    mesecnaCena: any;

    imaPPPlanova: boolean = false;
    seller: any = null;
    activeOrder: any = null;

    constructor(
        private activeOrderService: ActiveOrderService,
        private sellerService: SellersService,
        private paypalService: PaypalService,
        private route: ActivatedRoute,
        private router: Router,
        private sellersService: SellersService
    ) {
        this.route.params.subscribe((params: Params) => {
            const param = +params["id"];

            if (!isNaN(param)) {
                this.id = param;
                this.getActiveOrder();
            } else {
                Swal.fire({
                    icon: "error",
                    title: 'Greška',
                    text: 'Nije moguće dobaviti aktivnu porudžbinu.'
                });
            }
        });
    }

    ngOnInit() {}

    getActiveOrder() {
        this.activeOrderService.getActiveOrder(this.id).subscribe(
            success => {
                this.activeOrder = success;
                this.mesecnaCena = this.activeOrder.amount + (this.activeOrder.amount)/2;
                this.getPayPalSubscriptions(this.activeOrder.sellerId);
                this.sellersService.getWebsiteURL(this.activeOrder.sellerId).subscribe(
                    res => {
                      this.websiteURL = res;
                    }, err => {
                      Swal.fire({
                      icon: "error",
                      title: 'Greška',
                      text: 'Nije moguće dobaviti website link.'
                      });
                    }
                  );
            },
            error => {
                Swal.fire({
                    icon: "error",
                    title: 'Greška',
                    text: 'Došlo je do greške prilikom preuzimanja aktivne porudžbine.'
                  });
            }
        );
    }

    getPayPalSubscriptions(id) {
        this.paypalService.getPaypalSubscriptionPlans(id).subscribe(
            response => {
                this.PayPalPlans = response;
                if (
                    this.PayPalPlans !== undefined &&
                    this.PayPalPlans.length != 0
                ) {
                    this.ppizbor = this.PayPalPlans[0].id;
                    this.imaPPPlanova = true;
                }
            },
            error => {
                Swal.fire({
                    icon: "error",
                    title: 'Greška',
                    text: 'Došlo je do greške prilikom preuzimanja liste pretplata.'
                  });
            }
        );
    }

    onPPSub() {
        window.location.href = "https://localhost:4200/paypal/plan/subscribe/"
            .concat(this.ppizbor)
            .concat("/")
            .concat(this.id);
	}
	
	onRegularSub() {
		this.router.navigate(["/sellers/" + this.activeOrder.id]);
	}
}
