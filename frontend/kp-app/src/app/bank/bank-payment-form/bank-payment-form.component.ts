import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { BankService } from 'src/app/services/bank.service';

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
  transactionId: any;
  paymentConfirmation: boolean = false;
  
  constructor(private route: ActivatedRoute, private router: Router, private bankService: BankService) { 

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


  onValidate(){

    let bankAccountDTO = {
      pan: this.infoForm.value.pan,
      serviceCode: this.infoForm.value.serviceCode,
      cardholderName: this.infoForm.value.cardholderName,
      expirationDate: this.infoForm.value.expirationDate
    }

    this.validate(bankAccountDTO);
  }

  validate(bankAccountDTO){
    setTimeout(() =>{
      this.paymentConfirmation = true;
      alert("The information are valid");
    },1000)
    this.bankService.validateAndReserve(bankAccountDTO, this.transactionId).subscribe(
      (success) => {
        this.paymentConfirmation = true;
        alert("The information are valid");
      },
      (error: any) => {
        alert(error.message);
      }
    )
  }

  onPay(){

    let bankAccountDTO = {
      pan: this.infoForm.value.pan,
      serviceCode: this.infoForm.value.serviceCode,
      cardholderName: this.infoForm.value.cardholderName,
      expirationDate: this.infoForm.value.expirationDate
    }

    this.bankService.payment(bankAccountDTO, this.transactionId).subscribe(
      (response: any) => { 
        this.router.navigate(['bank/' + this.transactionId + '/success']);
      },
      (error) => {
        this.router.navigate(['bank/' + this.transactionId + '/failure']);
      }
    )
  }


}
