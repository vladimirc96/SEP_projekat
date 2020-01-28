import { Component, OnInit } from '@angular/core';
import { PaypalService } from '../services/paypal.service';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { CentralaService } from '../services/centrala.service';
import { SellersService } from '../services/sellers.service';
import { ActiveOrderService } from '../services/active-order.service';

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

  constructor(private palService: PaypalService, private route: ActivatedRoute, private router: Router, private aoService: ActiveOrderService) {
    this.route.params.subscribe(
      (params: Params) => {
        this.orderId = params['id'];
      }
    );
  }

  ngOnInit() {
    this.aoService.getActiveOrder(this.orderId).subscribe(
      (response) => {
        this.activeOrder = response;
      },
      (error) => {
        alert("error active order");
      }
    );
  }

  onProcceed() {
    this.status = true;

    let orderDTO = {
      price: this.activeOrder.amount,
      currency: this.activeOrder.currency,
      description: this.desc,
      id: this.activeOrder.sellerId,
      name: this.activeOrder.title
    }

    this.palService.pay(orderDTO).subscribe(
      (data) => {
        this.ret = data;
        window.location.href = this.ret;
      }, (error) => {
        alert("error");
      }
    )
  }
}
