import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { BankService } from 'src/app/services/bank.service';
import { CentralaService } from 'src/app/services/centrala.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { ActiveOrderService } from 'src/app/services/active-order.service';

@Component({
  selector: 'app-bank-payment',
  templateUrl: './bank-payment.component.html',
  styleUrls: ['./bank-payment.component.css']
})
export class BankPaymentComponent implements OnInit {
  
  activeOrder: any;
  activeOrderId: any;

  constructor(private activeOrderService: ActiveOrderService, private route: ActivatedRoute, private router: Router, private bankService: BankService, private spinner: NgxSpinnerService) { 

    this.route.params.subscribe((params: Params) => {
			const param = +params["id"];

			if (!isNaN(param)) {
				this.activeOrderId = param;
        this.activeOrderService.getActiveOrder(this.activeOrderId).subscribe(
          (response) => {
              this.activeOrder = response;
          },
          (error) => {
            alert(error.message);
          }
        )
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
        this.router.navigate([response.url + "/" + response.paymentId + "/form"]);
        this.spinner.hide();
      },
      (error) => {
        alert("Could not form the payment request.");
        this.spinner.hide();
        this.router.navigate(['']);
      }
    )



  }

}
