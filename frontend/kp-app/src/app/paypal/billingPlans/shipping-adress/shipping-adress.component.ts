import { Component, OnInit } from '@angular/core';
import { CentralaService } from 'src/app/services/centrala.service';
import { Router } from '@angular/router';
import { PaypalService } from 'src/app/services/paypal.service';
import { FormGroup, FormControl, Validators, NgForm } from '@angular/forms';

@Component({
  selector: 'app-shipping-adress',
  templateUrl: './shipping-adress.component.html',
  styleUrls: ['./shipping-adress.component.css']
})
export class ShippingAdressComponent implements OnInit {

  status: boolean = false;
  rad: any = null;
  ret: any;
  
  myForm: FormGroup;
  street: FormControl;
  city: FormControl;
  state: FormControl;
  postalCode: FormControl;
  countryCode: FormControl;

  constructor(private palService: PaypalService, private router: Router, private centralaService: CentralaService) {
    this.rad = this.centralaService.activeRad;
  }

  ngOnInit() {
    this.createFormControls();
    this.createForm();
  }

  createFormControls(){
    this.street = new FormControl('', Validators.required);
    this.city = new FormControl('', Validators.required)
    this.state = new FormControl('', Validators.required);
    this.postalCode = new FormControl('', Validators.required);
    this.countryCode = new FormControl('', Validators.required);
  }

  createForm() {
    this.myForm = new FormGroup({
      street: this.street,
      city: this.city,
      state: this.state,
      postalCode: this.postalCode,
      countryCode: this.countryCode
    });
  }

  onSubmitAdress(form: NgForm) {
    this.status = true;
    
    let shippingDTO = {
      street: this.myForm.value.street,
      city: this.myForm.value.city,
      state: this.myForm.value.state,
      postalCode: this.myForm.value.postalCode,
      countryCode: this.myForm.value.countryCode,
      id: this.rad.sellerId
    }
    console.log(shippingDTO);
    this.palService.createAgreement(shippingDTO).subscribe(
      (data) => {
        this.ret = data;
        window.location.href = this.ret;
        // window.open(this.ret, '_blank', 'toolbar=no,top=100,left=500,width=600,height=550');
      }, (error) => {
        alert("error");
      }
    );
  }

}
