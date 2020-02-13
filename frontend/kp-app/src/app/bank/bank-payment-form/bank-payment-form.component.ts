import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { BankService } from 'src/app/services/bank.service';
import { NgxSpinnerService } from 'ngx-spinner';
import Swal from 'sweetalert2'

@Component({
  selector: 'app-bank-payment-form',
  templateUrl: './bank-payment-form.component.html',
  styleUrls: ['./bank-payment-form.component.css']
})
export class BankPaymentFormComponent implements OnInit {

  infoForm = new FormGroup({
    pan: new FormControl(''),
    serviceCode: new FormControl(''),
    cardholderName: new FormControl(''),
    expirationDate: new FormControl('')
  })

  isValid: boolean = false;
  acquirerSuccess: boolean = false;
  issuerSuccess: boolean = false;

  responseDto: any;
  transactionId: any;
  paymentConfirmation: boolean = false;
  
  
  constructor(private spinner: NgxSpinnerService,private route: ActivatedRoute, private router: Router, private bankService: BankService) { 

    this.route.parent.params.subscribe((params: Params) => {
			const param = +params["id"];

			if (!isNaN(param)) {
				this.transactionId = param;
			} else {
				this.router.navigate(["/"]);
			}
		});

  }

  ngOnInit() {
  }

  onPay(){
    this.spinner.show();
    let bankAccountDTO = {
      pan: this.infoForm.value.pan,
      serviceCode: this.infoForm.value.serviceCode,
      cardholderName: this.infoForm.value.cardholderName,
      expirationDate: this.infoForm.value.expirationDate
    }

    this.bankService.payment(bankAccountDTO, this.transactionId).subscribe(
      (response: any) => {
        this.spinner.hide();
        this.router.navigate(['/bank/' + this.transactionId + '/success']);

      },
      (error: any) => {
        this.spinner.hide();
        if(error.status == '409'){
          this.router.navigate(['/bank/' + this.transactionId + '/failure']);
        }
        if(error.status == '400'){
          Swal.fire({
            icon: "error",
            title: 'Greška',
            text: 'Podaci koje ste uneli nisu validni. Pokušajte ponovo.'
          });
        }
      }
    )
  }
}
