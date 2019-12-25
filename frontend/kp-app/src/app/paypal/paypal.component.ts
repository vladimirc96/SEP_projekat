import { Component, OnInit } from '@angular/core';
import { PaypalService } from '../services/paypal.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgForm, FormGroup, Validators, FormControl} from '@angular/forms';

@Component({
  selector: 'app-paypal',
  templateUrl: './paypal.component.html',
  styleUrls: ['./paypal.component.css']
})
export class PaypalComponent implements OnInit {

  payForm: FormGroup;
  total: FormControl;
  currency: FormControl;
  intent: FormControl;
  description: FormControl;
  status: boolean = false;

  ret: any;
  constructor(private palService: PaypalService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.createFormControls();
    this.createForm();
  }

  createFormControls() {
    this.total = new FormControl('');
    this.currency = new FormControl('');
    this.intent = new FormControl('');
    this.description = new FormControl('');
  }

  createForm() {
    this.payForm = new FormGroup({
      total: this.total,
      currency: this.currency,
      intent: this.intent,
      description: this.description
    });
  }

  onSubmitPay(form: NgForm) {
    let orderDTO = {
      price: 100,
      currency: 'USD',
      intent: 'sale',
      description: ''
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
