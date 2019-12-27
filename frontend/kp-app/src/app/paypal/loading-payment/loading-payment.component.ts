import { Component, OnInit } from '@angular/core';
import { Router  } from '@angular/router';
import { PaypalService } from 'src/app/services/paypal.service';

@Component({
  selector: 'app-loading-payment',
  templateUrl: './loading-payment.component.html',
  styleUrls: ['./loading-payment.component.css']
})
export class LoadingPaymentComponent implements OnInit {

  ret: any;
  ref: any;

  constructor(private router: Router, private palService: PaypalService) {  }

  ngOnInit() {
    this.pass();
  }

  pass() {
    this.ref = this.router.url;
    const str: string[] = this.ref.split("?");
    const fin: string [] = str[1].split("&");
    const pmt = fin[0].split("=");
    const pyr = fin[2].split("=");
    const payment = pmt[1];
    const payer = pyr[1];
    this.palService.completePayment(payment, payer).subscribe(
      (data) => {
        this.ret = data;
        window.location.href = this.ret;
      }, (error) => {
        alert("error");
      }
    )
  }

}
