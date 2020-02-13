import { Component, OnInit } from '@angular/core';
import { CentralaService } from 'src/app/services/centrala.service';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { PaypalService } from 'src/app/services/paypal.service';
import { FormGroup, FormControl, Validators, NgForm } from '@angular/forms';
import { isNgTemplate } from '@angular/compiler';
import { ActiveOrderService } from 'src/app/services/active-order.service';

@Component({
  selector: 'app-shipping-adress',
  templateUrl: './shipping-adress.component.html',
  styleUrls: ['./shipping-adress.component.css']
})
export class ShippingAdressComponent implements OnInit {

  status: boolean = false;
  rad: any = null;
  ret: any;
  activeId: any;
  planId: any;
  NC: string = "http://localhost:4201";
  activeOrder: any;
  
  myForm: FormGroup;
  street: FormControl;
  city: FormControl;
  state: FormControl;
  postalCode: FormControl;
  countryCode: FormControl;

  constructor(private palService: PaypalService, private activeOrderService: ActiveOrderService, private router: Router, private route: ActivatedRoute) {
    this.route.params.subscribe((params: Params) => {
      const plan = +params["pl"];
      const activeId = +params["id"]

			if (!isNaN(activeId) || !isNaN(plan)) {
        this.activeId = activeId;
        this.planId = plan;
        this.getActiveOrder(activeId);
			} else {
				window.location.href = this.NC;
			}
		});
  }

  ngOnInit() {
    this.createFormControls();
    this.createForm();
  }

  getActiveOrder(id){
		this.activeOrderService.getActiveOrder(id).subscribe(
			(success) => {
        this.activeOrder = success;
			},
			error => alert(error.error),
		);
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
      planId: this.planId,
      activeOrderId: this.activeOrder.id,
      id: this.activeOrder.sellerId
    }
    console.log(shippingDTO);
    this.palService.createAgreement(shippingDTO, this.activeOrder.username).subscribe(
      (data) => {
        this.ret = data;
        window.location.href = this.ret;
        // window.open(this.ret, '_blank', 'toolbar=no,top=100,left=500,width=600,height=550');
      }, (error) => {
        alert("error");
      }
    );
  }

  onKeydown(e) {
    if(!((e.keyCode > 95 && e.keyCode < 106)
      || (e.keyCode > 47 && e.keyCode < 58) 
      || e.keyCode == 8 || e.keyCode == 37 || e.keyCode == 39 || e.keyCode == 9)) {
        return false;
    }
  }

}
