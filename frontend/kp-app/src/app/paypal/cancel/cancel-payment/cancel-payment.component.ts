import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PaypalService } from 'src/app/services/paypal.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-cancel-payment',
  templateUrl: './cancel-payment.component.html',
  styleUrls: ['./cancel-payment.component.css']
})
export class CancelPaymentComponent implements OnInit {

  ref: any;
  ret: any;
  websiteURL: string;

  constructor(private router: Router, private palService: PaypalService) {
    this.ref = this.router.url;
    const str: string[] = this.ref.split("?");
    const tkn: string[] = str[1].split("=");
    this.pass(tkn[1]);
  }

  ngOnInit() {
  }

  pass(token) {
    this.palService.cancelPayment(token).subscribe(
      (data) => {
        this.ret = data;
        this.websiteURL = this.ret.websiteLink;
      }, (error) => {
        Swal.fire({
          icon: "error",
          title: 'Greška',
          text: 'Došlo je do greške prilikom otkazivanja transakcije.'
        });
      }
    );
  }

  goHome() {
    window.location.href = this.websiteURL;
  }

}
