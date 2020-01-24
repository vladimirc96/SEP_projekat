import { Component, OnInit } from '@angular/core';
import { PaypalService } from 'src/app/services/paypal.service';
import { CentralaService } from 'src/app/services/centrala.service';
import { FormGroup, FormControl, Validators, NgForm } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-plan',
  templateUrl: './create-plan.component.html',
  styleUrls: ['./create-plan.component.css']
})
export class CreatePlanComponent implements OnInit {

  ret: any;
  rad: any = null;

  myForm: FormGroup;
  name: FormControl;
  description: FormControl;
  frequency: FormControl;
  freqInterval: FormControl;
  cycles: FormControl;
  amount: FormControl;
  currency: FormControl;
  amountStart: FormControl;

  constructor(private palService: PaypalService, private router: Router, private centralaService: CentralaService) {
    this.rad = this.centralaService.activeRad;
  }

  ngOnInit() {
    this.createFormControls();
    this.createForm();
  }

  createFormControls(){
    this.name = new FormControl(this.rad.title + " PLAN", Validators.required);
    this.description = new FormControl('', Validators.required)
    this.frequency = new FormControl("MONTH", Validators.required);
    this.freqInterval = new FormControl('', Validators.required);
    this.cycles = new FormControl('', Validators.required);
    this.amount = new FormControl(this.rad.price, Validators.required);
    this.currency = new FormControl("USD", Validators.required);
    this.amountStart = new FormControl(this.rad.price, Validators.required);
  }

  createForm() {
    this.myForm = new FormGroup({
      name: this.name,
      description: this.description,
      frequency: this.frequency,
      freqInterval: this.freqInterval,
      cycles: this.cycles,
      amount: this.amount,
      currency: this.currency,
      amountStart: this.amountStart
    });
  }

  onSubmitPlan(form: NgForm) {

      let planDTO = {
        name: this.myForm.value.name,
        description: this.myForm.value.description,
        frequency: this.myForm.value.frequency,
        freqInterval: this.myForm.value.freqInterval,
        cycles: this.myForm.value.cycles,
        amount: this.myForm.value.amount,
        currency: this.myForm.value.currency,
        amountStart: this.myForm.value.amountStart,
        merchantId: this.rad.sellerId
      }
  
      this.palService.createPlan(planDTO).subscribe(
        (data) => {
          this.ret = data;
          if(this.ret === "PlanCreated") {
            alert("Plan created!");
            this.router.navigate(['/centrala']);
          } else {
            alert("Plan WASN'T created");
          }
        }, (error) => {
          alert("error");
        }
      );
  }

}
