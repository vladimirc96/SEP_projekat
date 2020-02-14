import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PaypalService } from 'src/app/services/paypal.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-execute-plan',
  templateUrl: './execute-plan.component.html',
  styleUrls: ['./execute-plan.component.css']
})
export class ExecutePlanComponent implements OnInit {

  ref: any;
  ret: any;
  showInvalid: boolean = false;
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
    this.palService.executePlan(token).subscribe(
      (data) => {
        this.ret = data;
        if(this.ret === "success") {
          Swal.fire({
            icon: "success",
            title: 'Uspešno',
            text: 'Pretplaćeni ste na plan.'
          });
          window.location.href = this.websiteURL;
        } else {
          Swal.fire({
            icon: "error",
            title: 'Greška',
            text: 'Došlo je do greške prilikom izvršavanja pretplate.'
          });
        }
      }, (error) => {
        Swal.fire({
          icon: "error",
          title: 'Greška',
          text: 'Došlo je do greške prilikom izvršavanja pretplate.'
        });
        window.location.href = this.websiteURL;
      }
    )
  }

  goHome() {
    window.location.href = "http://localhost:4202";
  }

}
