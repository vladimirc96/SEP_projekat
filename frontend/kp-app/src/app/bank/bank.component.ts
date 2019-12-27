import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormControlDirective } from '@angular/forms';
import { BankService } from '../services/bank.service';
import { CentralaService } from '../services/centrala.service';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-bank',
  templateUrl: './bank.component.html',
  styleUrls: ['./bank.component.css']
})
export class BankComponent implements OnInit {

  rad: any = null;
  sellerId: any;

  constructor(private centralaService: CentralaService, private route: ActivatedRoute, private router: Router, private bankService: BankService, private spinner: NgxSpinnerService) { 
    this.rad = this.centralaService.activeRad;

    this.route.params.subscribe((params: Params) => {
			const param = +params["id"];

			if (!isNaN(param)) {
				this.sellerId = param;
			} else {
				this.router.navigate(["/"]);
			}
		});

  }

  ngOnInit() {
  }


  redirect(){
    this.spinner.show();

    let paymentDTO = {
      sellerId: this.sellerId,
      amount: this.rad.price
    }

    this.bankService.paymentRequest(paymentDTO).subscribe(
      (response: any) => {
        this.router.navigate([response.url + "/" + response.paymentId]);
        this.spinner.hide();
      },
      (error) => {
        alert("Could not form the payment request.");
      }
    )



  }

}
