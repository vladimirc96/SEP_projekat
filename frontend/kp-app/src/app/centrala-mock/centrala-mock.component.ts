import { Component, OnInit } from "@angular/core";
import { CentralaService } from '../services/centrala.service';
import { Router } from '@angular/router';
import { TouchSequence } from 'selenium-webdriver';

@Component({
  selector: "app-centrala-mock",
  templateUrl: "./centrala-mock.component.html",
  styleUrls: ["./centrala-mock.component.css"]
})
export class CentralaMockComponent implements OnInit {

	radovi: any[] = null;
	constructor(private centralaService: CentralaService, private router: Router) {
		this.radovi = centralaService.radovi;
	}

	ngOnInit() {}

	onBuy(r) {
		this.centralaService.activeRad = r;
		this.router.navigate(['/sellers', r.sellerId]);

	}

	onPlan(r) {
		this.centralaService.activeRad = r;
		this.router.navigate(['/paypal/plan']);
	}


}
