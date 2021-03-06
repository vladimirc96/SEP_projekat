import { Component, OnInit } from '@angular/core';
import { Router  } from '@angular/router';
import { PaypalService } from 'src/app/services/paypal.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-loading-payment',
  templateUrl: './loading-payment.component.html',
  styleUrls: ['./loading-payment.component.css']
})
export class LoadingPaymentComponent implements OnInit {

  ret: any;
  ref: any;
  validna: any;
  payment: any;
  payer: any;
  showInvalid: boolean = false;
  websiteURL: string;

  constructor(private router: Router, private palService: PaypalService) {
  }

  ngOnInit() {
    this.ref = this.router.url;
    const str: string[] = this.ref.split("?");
    const fin: string [] = str[1].split("&");
    const pmt = fin[0].split("=");
    const pyr = fin[2].split("=");
    this.payment = pmt[1];
    this.payer = pyr[1];

    this.palService.checkStatus(this.payment).subscribe(
      (data) => {
        this.validna = data;
        this.websiteURL = this.validna.websiteLink;
        if(this.validna.text === "valid") {
          this.pass();
        } else {
          this.showInvalid = true;
        }
      }, (error) => {
        Swal.fire({
          icon: "error",
          title: 'Greška',
          text: 'Došlo je do greške prilikom provere statusa transakcije.'
        });
      }
    );
  }

  pass() {
    this.palService.completePayment(this.payment, this.payer).subscribe(
      (data) => {
        this.ret = data;
        window.location.href = this.ret;
      }, (error) => {
        Swal.fire({
          icon: "error",
          title: 'Greška',
          text: 'Došlo je do greške prilikom završavanja transakcije.'
        });
      }
    );
  }

  goHome() {
    window.location.href = this.websiteURL;
  }

}
