import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PaypalService } from 'src/app/services/paypal.service';

@Component({
  selector: 'app-cancel-plan',
  templateUrl: './cancel-plan.component.html',
  styleUrls: ['./cancel-plan.component.css']
})
export class CancelPlanComponent implements OnInit {
  ref: any;
  ret: any;

  constructor(private router: Router, private palService: PaypalService) {
    this.ref = this.router.url;
    const str: string[] = this.ref.split("?");
    const tkn: string[] = str[1].split("=");
    this.pass(tkn[1]);
  }

  ngOnInit() {
  }

  pass(token) {
    this.palService.cancelSubscription(token).subscribe(
      (data) => {
        this.ret = data;
      }, (error) => {
        alert("error");
      }
    );
  }

  goHome() {
    window.location.href = "http://localhost:4201/";
  }
}
