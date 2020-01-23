import { Component, OnInit } from "@angular/core";
import { SellersService } from 'src/app/services/sellers.service';
import { ActivatedRoute, Params } from '@angular/router';
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
	showInfo = false;
	showForm = true;

	registerResponse: any = null;


	registrationForm = this.fb.group({
		id: ["", Validators.required],
		email: ["", [Validators.required, Validators.email]],
		password: ["", Validators.required],
		name: [""],
		organization: [""],
		paymentMethods: this.fb.array,
		
	  });

    constructor(private route: ActivatedRoute, private sellersService: SellersService, private fb: FormBuilder) {
		this.route.params.subscribe(
			(params: Params) => {
			  this.sellerId = +params['sellerId'];
			  this.fillForm();
			}
		  );

		this.paymentMethods = this.sellersService.paymentMethods;
	 }

    ngOnInit() {
		this.renderPMCheckboxes();
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

		console.log(dto);

		if (dto.paymentMethods.length < 1) {
			this.errorMessage = "Morate izabrati bar jedan servis za plaćanje."
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
		console.log(pm);
	}
}
