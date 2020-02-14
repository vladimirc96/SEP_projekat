import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { BankService } from 'src/app/services/bank.service';
import { CentralaService } from 'src/app/services/centrala.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { ActiveOrderService } from 'src/app/services/active-order.service';
import Swal from 'sweetalert2'

@Component({
  selector: 'app-bank-payment',
  templateUrl: './bank-payment.component.html',
  styleUrls: ['./bank-payment.component.css']
})
export class BankPaymentComponent implements OnInit {
  
  rad: any = null;
  id: any;
  activeOrder: any;

  constructor(private activeOrderSerivce: ActiveOrderService, private route: ActivatedRoute, private router: Router, private bankService: BankService, private spinner: NgxSpinnerService) { 

    this.route.params.subscribe((params: Params) => {
			const param = +params["id"];

			if (!isNaN(param)) {
        this.id = param;
        this.activeOrderSerivce.getActiveOrder(this.id).subscribe(
          (response: any) => {
            if (response.orderStatus !== "CREATED") {
              this.orderProcessedByAnotherServiceError();
            }
            this.activeOrder = response;
          },
          (error) => {
            alert(error.message);
          }
        )

			} else {
				this.router.navigate(["/"]);
			}
		});

  }

  ngOnInit() {
  }


  redirect(){
    this.spinner.show();
    this.bankService.paymentRequest(this.activeOrder).subscribe(
      (response: any) => {
        this.router.navigate([response.url + "/" + response.paymentId + "/form"]);
        this.spinner.hide();
      },
      (error) => {
        Swal.fire({
          icon: "error",
          title: 'Greška',
          text: 'Nije moguće formirati zahtev za plaćanje.'
        });
        this.spinner.hide();
        this.router.navigate(['']);
      }
    )



  }

  orderProcessedByAnotherServiceError() {
    Swal.fire({
      icon: "error",
      title: 'Greška',
      text: 'Porudžbina se obrađuje od strane drugog servisa.'
    });
		this.goHome();
	}

	goHome() {
		window.location.href = "http://localhost:4201/";
	}

}
