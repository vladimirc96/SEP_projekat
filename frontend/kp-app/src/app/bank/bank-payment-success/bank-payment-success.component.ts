import { Component, OnInit } from '@angular/core';
import { BankService } from 'src/app/services/bank.service';
import { ActivatedRoute, Params } from '@angular/router';

@Component({
  selector: 'app-bank-payment-success',
  templateUrl: './bank-payment-success.component.html',
  styleUrls: ['./bank-payment-success.component.css']
})
export class BankPaymentSuccessComponent implements OnInit {

  baseUrl: string;

  constructor(private bankService: BankService, private route: ActivatedRoute) {
    this.route.parent.params.subscribe((params: Params) => {
			const param = +params["id"];

			if (!isNaN(param)) {
        this.bankService.getBaseUrl(param).subscribe(
          (response) => {
            this.baseUrl = response;
          }
        ),
        (error) => { console.log(error.message) }
      }
      
		});

   }

  ngOnInit() {
  }

  goHome() {
    window.location.href = this.baseUrl;
  }
  
}
