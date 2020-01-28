import { Component, OnInit } from "@angular/core";
import { SellersService } from 'src/app/services/sellers.service';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';

@Component({
    selector: "app-seller-registration",
    templateUrl: "./seller-registration.component.html",
    styleUrls: ["./seller-registration.component.css"]
})
export class SellerRegistrationComponent implements OnInit {

	sellerId: number;
	paymentMethods = [];
	errorMessage = null;


	showBankForm = false;
	showPPForm = false;
	showBTCForm = false;

	activePm = null;
	

	registerResponse: any = null;


	registrationForm = this.fb.group({
		id: ["", Validators.required],
		email: ["", [Validators.required, Validators.email]],
		password: ["", Validators.required],
		name: [""],
		organization: [""],
		paymentMethods: this.fb.array,
		
	  });

    constructor(private route: ActivatedRoute, private sellersService: SellersService, private fb: FormBuilder, private router: Router) {
		this.route.params.subscribe(
			(params: Params) => {
			  this.sellerId = +params['sellerId'];
			  this.fetchSeller();
			  this.fillForm();
			}
		  );

		this.paymentMethods = this.sellersService.paymentMethods;
	 }

    ngOnInit() {
		this.renderPMCheckboxes();
	}

	fetchSeller() {
		this.sellersService.getSeller(this.sellerId).subscribe(
			(res: any) => {
				// seller data submited
				if (res.email) {
					this.registerResponse = res;
				}
			}, err =>{
				this.router.navigate(['/']);
			}
		)
	}


	renderPMCheckboxes() {
		this.registrationForm.setControl(
			"paymentMethods",
			this.mapToCheckboxArrayGroup()
		);
	}

	mapToCheckboxArrayGroup() {
		return this.fb.array(
			this.paymentMethods.map(item => {
				return this.fb.group({
						id: item.id,
						selected: [false, ""]
				});
			})
		)
	}

	fillForm() {
		this.registrationForm.patchValue({
			id: this.sellerId
		})
	}

	private getSelectedItems(formArray) {
		return formArray.filter(item => item.selected);
	}

	onSubmit() {

		this.errorMessage = null;

		let dto = {
			id: this.sellerId,
			email: this.registrationForm.get('email').value,
			password: this.registrationForm.get('password').value,
			organization: this.registrationForm.get('organization').value,
			name: this.registrationForm.get('name').value,
			paymentMethods: this.getSelectedItems(this.registrationForm.get('paymentMethods').value)
		}


		if (dto.paymentMethods.length < 1) {
			this.errorMessage = "Morate izabrati bar jedan servis za plaćanje."
			return;
		}

		this.sellersService.register(dto).subscribe(
			(res: any) => {
				this.registerResponse = res;
			}, 
			err => {
				console.log(err.error)
			}
		)
	}

	continueRegistration(pm) {

		this.activePm = null;

		this.showBankForm = false;
		this.showPPForm = false;
		this.showBTCForm = false;
		
		if (pm.id === 1) {
			this.activePm = pm;
			this.showBankForm = true;
		} else if (pm.id === 2) {
			this.activePm = pm;
			this.showPPForm = true;
		} else if (pm.id === 3) {
			this.activePm = pm;
			console.log(this.activePm);
			this.showBTCForm = true;
		}
	}

	onEmitBTC($event) {
		console.log('gerwe');
		if ($event) {
			this.showBTCForm = false;
		}

		this.registerResponse.paymentMethods.forEach(pm => {
			if (pm.id === 3) {
				pm.registerSuccess = true;
			}
		});
	}

	onEmitBank($event){
		if ($event) {
			this.showBankForm = false;
		}

		this.registerResponse.paymentMethods.forEach(pm => {
			if (pm.id === 1) {
				pm.registerSuccess = true;
			}
		});
	}

	onEmitPayPal($event){
		if ($event) {
			this.showPPForm = false;
		}

		this.registerResponse.paymentMethods.forEach(pm => {
			if (pm.id === 2) {
				pm.registerSuccess = true;
			}
		});
	}
}
