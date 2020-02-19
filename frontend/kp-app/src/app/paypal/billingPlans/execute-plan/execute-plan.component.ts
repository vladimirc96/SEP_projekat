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
        this.ret = JSON.parse(data);
        this.websiteURL = this.ret.websiteLink;
        console.log(this.ret);
        if(this.ret.text == "success") {
          Swal.fire({
            icon: "success",
            title: 'Uspešno',
            text: 'Pretplaćeni ste na plan.'
          });
          console.log("OK!!!!")
          setTimeout(() => {
           window.location.href = this.ret.websiteLink;
          }, 3000);
        } else {
          
          console.log("ELSE 1")
          Swal.fire({
            icon: "error",
            title: 'Greška1',
            text: 'Došlo je do greške prilikom izvršavanja pretplate.'
          });
          setTimeout(() => {
            window.location.href = this.ret.websiteLink;
           }, 3000);
        }
      }, (error) => {
        
        setTimeout(() => {
          window.location.href = this.ret.websiteLink;
         }, 3000);
        Swal.fire({
          icon: "error",
          title: 'Greška2',
          text: 'Došlo je do greške prilikom izvršavanja pretplate.'
        });
      }
    )
  }

  goHome() {
    window.location.href = this.websiteURL;
  }

}
