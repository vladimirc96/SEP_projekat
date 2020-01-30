import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-bank-payment-success',
  templateUrl: './bank-payment-success.component.html',
  styleUrls: ['./bank-payment-success.component.css']
})
export class BankPaymentSuccessComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  goHome() {
    window.location.href = "http://localhost:4201";
  }
  
}
