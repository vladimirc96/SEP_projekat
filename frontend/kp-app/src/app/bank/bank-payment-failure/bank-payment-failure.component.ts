import { Component, OnInit } from '@angular/core';
import { BankService } from 'src/app/services/bank.service';
import { ActivatedRoute, Params } from '@angular/router';

@Component({
  selector: 'app-bank-payment-failure',
  templateUrl: './bank-payment-failure.component.html',
  styleUrls: ['./bank-payment-failure.component.css']
})
export class BankPaymentFailureComponent implements OnInit {

  baseUrl: string;

  constructor(private bankService: BankService, private route: ActivatedRoute) {

    this.route.parent.params.subscribe((params: Params) => {
      const param = +params["id"];
      console.log(param);

			if (!isNaN(param)) {
        this.bankService.getBaseUrl(param).subscribe(
          (response) => {
            this.baseUrl = response;
            console.log(this.baseUrl);
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
