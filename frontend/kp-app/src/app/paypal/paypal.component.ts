import { Component, OnInit } from '@angular/core';
import { PaypalService } from '../services/paypal.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CentralaService } from '../services/centrala.service';

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

  constructor(private palService: PaypalService, private route: ActivatedRoute, private router: Router, private centralaService: CentralaService) {
    this.rad = this.centralaService.activeRad;
  }

  ngOnInit() {
  }

  procceed() {
    this.status!=this.status;
    let orderDTO = {
      price: this.rad.price,
      currency: 'USD',
      description: this.desc,
      id: this.rad.sellerId
    }

    this.palService.pay(orderDTO).subscribe(
      (data) => {
        this.ret = data;
        window.location.href = this.ret;
      }, (error) => {
        alert("error count");
      }
    )
  }

}
