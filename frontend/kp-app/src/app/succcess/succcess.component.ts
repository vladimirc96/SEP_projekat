import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { SellersService } from '../services/sellers.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-succcess',
  templateUrl: './succcess.component.html',
  styleUrls: ['./succcess.component.css']
})
export class SucccessComponent implements OnInit {

  sellerID: any;
  websiteURL: string;

  constructor(private route: ActivatedRoute, private sellersService: SellersService) {
    this.route.params.subscribe(
      (params: Params) => {
        this.sellerID = +params['id'];
      }
    );

    this.sellersService.getWebsiteURL(this.sellerID).subscribe(
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
  }

  ngOnInit() {
  }

  goHome() {
    window.location.href = this.websiteURL;
  }


}
