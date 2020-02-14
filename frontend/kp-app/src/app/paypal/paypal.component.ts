import { Component, OnInit } from '@angular/core';
import { PaypalService } from '../services/paypal.service';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { ActiveOrderService } from '../services/active-order.service';
import Swal from 'sweetalert2';
import { SellersService } from '../services/sellers.service';

@Component({
  selector: 'app-paypal',
  templateUrl: './paypal.component.html',
  styleUrls: ['./paypal.component.css']
})
export class PaypalComponent implements OnInit {

  status: boolean = false;
  rad: any = null;
  ret: any;
  desc: String = "";
  opis: boolean = false;
  orderId: any;
  activeOrder: any = null;
  websiteURL: string;

  constructor(private palService: PaypalService, private sellersService: SellersService, private route: ActivatedRoute, private router: Router, private aoService: ActiveOrderService) {
    this.route.params.subscribe(
      (params: Params) => {
        this.orderId = params['id'];
      }
    );
  }

  ngOnInit() {
    this.aoService.getActiveOrder(this.orderId).subscribe(
      (response: any) => {
        if (response.orderStatus !== "CREATED") {
					this.orderProcessedByAnotherServiceError();
				}
        this.activeOrder = response;
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
      (error) => {
        Swal.fire({
          icon: "error",
          title: 'Greška',
          text: 'Došlo je do greške prilikom preuzimanja aktivne porudžbine.'
        });
      }
    );
  }

  orderProcessedByAnotherServiceError() {
    Swal.fire({
      icon: "warning",
      text: 'Porudžbina je u fazi procesiranja od strane drugog servisa.'
    });
		this.goHome();
	}

	goHome() {
		window.location.href = this.websiteURL;
	}

  onProcceed() {
    this.status = true;

    let orderDTO = {
      price: this.activeOrder.amount,
      currency: this.activeOrder.currency,
      description: this.desc,
      id: this.activeOrder.sellerId,
      name: this.activeOrder.title,
      activeOrderId: this.activeOrder.id
    }

    this.palService.pay(orderDTO).subscribe(
      (data) => {
        this.ret = data;
        window.location.href = this.ret;
      }, (error) => {
        Swal.fire({
          icon: "error",
          title: 'Greška',
          text: 'Došlo je do greške prilikom plaćanja.'
        });
      }
    )
  }
}
