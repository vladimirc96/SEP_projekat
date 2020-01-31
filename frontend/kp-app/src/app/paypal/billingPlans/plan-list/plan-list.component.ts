import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { PaypalService } from 'src/app/services/paypal.service';

@Component({
  selector: 'app-plan-list',
  templateUrl: './plan-list.component.html',
  styleUrls: ['./plan-list.component.css']
})
export class PlanListComponent implements OnInit {
  magazineId: any;
  billingPlans: any = null;
  pribavio: boolean = false;
  imaPlanova: boolean = false;

  constructor(private paypalService: PaypalService, private route: ActivatedRoute) {
    this.route.params.subscribe(
      (params: Params) => {
        this.magazineId = params['id'];
      }
    );
    
  }

  ngOnInit() {
    this.paypalService.getAllPlans(this.magazineId).subscribe(
      (response) => {
        this.pribavio = true;
        this.billingPlans = response;
        if(this.billingPlans.length != 0) {
          this.imaPlanova = true;
        }
      },
      (error) => {
        alert(error.message);
      }
    );
  }

  goHome() {
    window.location.href = "http://localhost:4201";
  }

}
