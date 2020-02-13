import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';

@Component({
  selector: 'app-new-payment-method',
  templateUrl: './new-payment-method.component.html',
  styleUrls: ['./new-payment-method.component.css']
})
export class NewPaymentMethodComponent implements OnInit {

  isRegistered: boolean = false;

  paymentMethodForm = new FormGroup({
    serviceName: new FormControl(''),
    serviceBaseUrl: new FormControl(''),
  })

  constructor() { }

  ngOnInit() {
  }

  onSubmit(){

    let paymentMethodDTO = {
      serviceName: this.paymentMethodForm.value.serviceName,
      serviceBaseUrl: this.paymentMethodForm.value.serviceBaseUrl
    }

    this.isRegistered = true;

  }

  goHome(){
    this.isRegistered=false;
  }


}
