import { Component, OnInit } from '@angular/core';
import { PaypalService } from 'src/app/services/paypal.service';
import { CentralaService } from 'src/app/services/centrala.service';
import { FormGroup, FormControl, Validators, NgForm } from '@angular/forms';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { SellersService } from 'src/app/services/sellers.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-create-plan',
  templateUrl: './create-plan.component.html',
  styleUrls: ['./create-plan.component.css']
})
export class CreatePlanComponent implements OnInit {

  ret: any;
  rad: any = null;
  id: any;
  status: boolean = false;

  myForm: FormGroup;
  name: FormControl;
  description: FormControl;
  frequency: FormControl;
  freqInterval: FormControl;
  cycles: FormControl;
  amount: FormControl;
  currency: FormControl;
  amountStart: FormControl;

  constructor(private palService: PaypalService, private router: Router, private sellersService: SellersService, private route: ActivatedRoute) {
    this.route.params.subscribe((params: Params) => {
			const param = +params["id"];

			if (!isNaN(param)) {
        this.id = param;
        
			} else {
				this.router.navigate(["/"]);
			}
    });
  }

  ngOnInit() {
    this.sellersService.getActivePlan(this.id).subscribe(
      (data) => {
        this.rad = data;
        this.createFormControls();
        this.createForm();
        console.log(this.rad);
      }, (error) => {
          alert("error getting active plan");
      }
    );
    
  }

  createFormControls(){
    this.name = new FormControl(this.rad.name + " PLAN", Validators.required);
    this.description = new FormControl('', Validators.required);
    this.frequency = new FormControl("MONTH", Validators.required);
    this.freqInterval = new FormControl('', Validators.required);
    this.cycles = new FormControl('', Validators.required);
    this.amount = new FormControl(this.rad.amount, Validators.required);
    this.currency = new FormControl(this.rad.currency, Validators.required);
    this.amountStart = new FormControl(this.rad.amount, Validators.required);
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
    this.status = true;

    let planDTO = {
      name: this.myForm.value.name,
      description: this.myForm.value.description,
      frequency: this.myForm.value.frequency,
      freqInterval: this.myForm.value.freqInterval,
      cycles: this.myForm.value.cycles,
      amount: this.myForm.value.amount,
      currency: this.myForm.value.currency,
      amountStart: this.myForm.value.amount,
      merchantId: this.rad.sellerId
    }

    this.palService.createPlan(planDTO).subscribe(
      (data) => {
        this.ret = data;
        if(this.ret === "PlanCreated") {
          this.sellersService.removeActivePlan(this.id).subscribe(
              (data) => {
                Swal.fire({
                  icon: "success",
                  title: 'Uspešno',
                  text: 'Novi plan je kreiran.'
                });
                window.location.href = "http://localhost:4201/";
              }, (error) => {
                Swal.fire({
                  icon: "error",
                  title: 'Greška',
                  text: 'Došlo je do greške prilikom kreiranja novog plana.'
                });
              }
            );
        } else {
          alert("Plan WASN'T created");
        }
      }, (error) => {
        alert("error");
      }
    );
  }

  onKeydown(e) {
    if(!((e.keyCode > 95 && e.keyCode < 106)
      || (e.keyCode > 47 && e.keyCode < 58) 
      || e.keyCode == 8 || e.keyCode == 37 || e.keyCode == 39)) {
        return false;
    }
  }

}
