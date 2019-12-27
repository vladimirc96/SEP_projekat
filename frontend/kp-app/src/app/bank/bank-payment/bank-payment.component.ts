import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { BankService } from 'src/app/services/bank.service';

@Component({
  selector: 'app-bank-payment',
  templateUrl: './bank-payment.component.html',
  styleUrls: ['./bank-payment.component.css']
})
export class BankPaymentComponent implements OnInit {
  
  infoForm = new FormGroup({
    pan: new FormControl(''),
    serviceCode: new FormControl(''),
    cardholderName: new FormControl(''),
    expirationDate: new FormControl('')
  })

  isValid: boolean = false;
  transactionId: any;
  
  constructor(private route: ActivatedRoute, private router: Router, private bankService: BankService) { 

    this.route.params.subscribe((params: Params) => {
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
      this.isValid = true;
      alert("The information are valid");
    },1000)
    this.bankService.validate(bankAccountDTO, this.transactionId).subscribe(
      (success) => {
        this.isValid = true;
        alert("The information are valid");
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
        this.router.navigate(['bank-payment/' + this.transactionId + '/success']);
      },
      (error) => {
        this.router.navigate(['bank-payment/' + this.transactionId + '/failure']);
      }
    )
  }



}
