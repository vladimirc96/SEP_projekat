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
  acquirerSuccess: boolean = false;
  issuerSuccess: boolean = false;

  responseDto: any;
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
    this.bankService.validateAndReserve(bankAccountDTO, this.transactionId).subscribe(
      (response) => {
        this.paymentConfirmation = true;
        this.responseDto = response;
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

    if(this.responseDto.hasOwnProperty('acquirerOrderId') && this.responseDto.hasOwnProperty('issuerOrderId')){
      
      this.confirmPaymentAcquirer(bankAccountDTO);
      this.confirmPaymentIssuer(bankAccountDTO);
      if(this.acquirerSuccess == true && this.issuerSuccess == true){
        this.router.navigate(['bank/' + this.transactionId + '/success']);
      }else{
        this.router.navigate(['bank/' + this.transactionId + '/failure']);
      }


    }else{

      this.confirmPaymentAcquirer(bankAccountDTO);
      if(this.acquirerSuccess == true){
        this.router.navigate(['bank/' + this.transactionId + '/success']);
      }else{
        this.router.navigate(['bank/' + this.transactionId + '/failure']);
      }

    }
    
  }


  confirmPaymentAcquirer(bankAccountDTO){
    this.bankService.confirmPaymentAcquirer(bankAccountDTO, this.transactionId).subscribe(
      (response: any) => { 
        alert("Success");
        this.acquirerSuccess = true;
      },
      (error) => {
        alert("Fail");
        this.acquirerSuccess = false;
      }
    )
  }

  confirmPaymentIssuer(bankAccountDTO){
    this.bankService.confirmPaymentIssuer(bankAccountDTO, this.transactionId).subscribe(
      (response: any) => {
        alert("Success"); 
        this.issuerSuccess = true;
      },
      (error) => {
        alert("Fail");
        this.issuerSuccess = false;
      }
    )
  }


}
