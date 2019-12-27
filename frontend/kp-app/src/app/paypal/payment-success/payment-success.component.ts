import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-payment-success',
  templateUrl: './payment-success.component.html',
  styleUrls: ['./payment-success.component.css']
})
export class PaymentSuccessComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  goHome() {
    window.location.href = "https://localhost:4200/centrala";
  }

}
