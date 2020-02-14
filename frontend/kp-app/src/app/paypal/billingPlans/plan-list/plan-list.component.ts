import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { PaypalService } from 'src/app/services/paypal.service';
import Swal from 'sweetalert2';
import { SellersService } from 'src/app/services/sellers.service';

@Component({
  selector: 'app-plan-list',
  templateUrl: './plan-list.component.html',
  styleUrls: ['./plan-list.component.css']
})
export class PlanListComponent implements OnInit {
  sellerId: any;
  billingPlans: any = null;
  pribavio: boolean = false;
  imaPlanova: boolean = false;

  websiteURL: string;

  constructor(private paypalService: PaypalService, private sellersService: SellersService, private route: ActivatedRoute) {
    this.route.params.subscribe(
      (params: Params) => {
        this.sellerId = params['id'];
      }
    );
    
  }

  ngOnInit() {
    this.paypalService.getAllPlans(this.sellerId).subscribe(
      (response) => {
        this.pribavio = true;
        this.billingPlans = response;
        if(this.billingPlans.length != 0) {
          this.imaPlanova = true;
        }
        this.sellersService.getWebsiteURL(this.sellerId).subscribe(
          res => {
            this.websiteURL = res;
          }, err => {
            Swal.fire({
              icon: "error",
              title: 'Greška',
              text: 'Nije moguće dobaviti website link.'
            });
          }
        );
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
    window.location.href = this.websiteURL;
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
