import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PaypalService } from 'src/app/services/paypal.service';

@Component({
  selector: 'app-execute-plan',
  templateUrl: './execute-plan.component.html',
  styleUrls: ['./execute-plan.component.css']
})
export class ExecutePlanComponent implements OnInit {

  ref: any;
  ret: any;
  showInvalid: boolean = false;

  constructor(private router: Router, private palService: PaypalService) {
    this.ref = this.router.url;
    const str: string[] = this.ref.split("?");
    const tkn: string[] = str[1].split("=");
    this.pass(tkn[1]);
  }

  ngOnInit() {
  }

  pass(token) {
    this.palService.executePlan(token).subscribe(
      (data) => {
        this.ret = data;
        if(this.ret === "success") {
          alert("You are now subscribed!");
          window.location.href = "http://localhost:4201";
        } else {
          alert("Error while executing subscription plan.");
        }
      }, (error) => {
        alert("error");
        window.location.href = "http://localhost:4201";
      }
    )
  }

  goHome() {
    window.location.href = "http://localhost:4201";
  }

}
