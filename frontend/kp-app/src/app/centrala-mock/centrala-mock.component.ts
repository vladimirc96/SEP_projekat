import { Component, OnInit } from "@angular/core";
import { CentralaService } from '../services/centrala.service';
import { Router } from '@angular/router';
import { TouchSequence } from 'selenium-webdriver';
import { SellersService } from '../services/sellers.service';

@Component({
  selector: "app-centrala-mock",
  templateUrl: "./centrala-mock.component.html",
  styleUrls: ["./centrala-mock.component.css"]
})
export class CentralaMockComponent implements OnInit {

	radovi: any[] = null;
	constructor(private centralaService: CentralaService, private router: Router, private sellersService: SellersService) {
		this.radovi = centralaService.radovi;
	}

	ngOnInit() {}

	onBuy(r) {
		this.centralaService.activeRad = r;
		this.router.navigate(['/sellers', r.sellerId]);

	}

	onRegister() {
		this.sellersService.initRegister().subscribe(
			(res: any) => {
				this.router.navigate(['/reg/' + res.id]);
			}, error => console.log(error.error)
		)
	}


}
