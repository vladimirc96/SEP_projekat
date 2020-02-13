import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { PaypalService } from 'src/app/services/paypal.service';
import Swal from 'sweetalert2';

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

  lclhst: string = "http://localhost:4204";

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
        Swal.fire({
          icon: "error",
          title: 'Greška',
          text: 'Došlo je do greške prilikom preuzimanja svih planova.'
        });
      }
    );
  }

  goHome() {
    window.location.href = this.lclhst;
  }

  onCancelPlan(id, seller) {
    this.paypalService.cancelBillingPlan(id, seller).subscribe(
      res => {
        if(res === "done") {
          let i = this.billingPlans.findIndex(plan => plan.id === id);
          this.billingPlans.splice(i, 1);
          Swal.fire({
            icon: "success",
            title: 'Uspešno',
            text: 'Izabrani plan je otkazan i postavljen u neaktivno stanje.'
          });
        }
      }, err => {
        Swal.fire({
          icon: "error",
          title: 'Greška',
          text: 'Došlo je do greške prilikom otkazivanja plana.'
        });
      }
    );
  }

}
