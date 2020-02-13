import { Component, OnInit, ViewChild, ComponentFactoryResolver } from "@angular/core";
import { SellersService } from "src/app/services/sellers.service";
import { ActivatedRoute, Params, Router } from "@angular/router";
import { FormBuilder, Validators } from "@angular/forms";
import { PmService } from "src/app/services/pm.service";
import { PmComponent } from "src/app/model/pm-component.model";
import { PmDirective } from 'src/app/directives/pm.directive';
import {  IRegistrationComponent } from 'src/app/interfaces/i-registration.component';
import { Subscription } from 'rxjs';

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
        paymentMethods: this.fb.array
    });

    constructor(
        private route: ActivatedRoute,
        private sellersService: SellersService,
        private pmService: PmService,
        private fb: FormBuilder,
		private router: Router,
		private componentFactoryResolver: ComponentFactoryResolver
    ) {
        this.route.params.subscribe((params: Params) => {
            this.sellerId = +params["sellerId"];
            this.fetchSeller();
            this.fillForm();
        });

       
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
					this.paymentMethods = res.paymentMethods;
                }
            },
            err => {
                this.router.navigate(["/"]);
            }
        );
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
        );
    }

    fillForm() {
        this.registrationForm.patchValue({
            id: this.sellerId
        });
    }

    private getSelectedItems(formArray) {
        return formArray.filter(item => item.selected);
    }

    onSubmit() {
        this.errorMessage = null;

        let dto = {
            id: this.sellerId,
            email: this.registrationForm.get("email").value,
            password: this.registrationForm.get("password").value,
            organization: this.registrationForm.get("organization").value,
            name: this.registrationForm.get("name").value
            // paymentMethods: this.getSelectedItems(this.registrationForm.get('paymentMethods').value)
        };

        // if (dto.paymentMethods.length < 1) {
        // 	this.errorMessage = "Morate izabrati bar jedan servis za plaćanje."
        // 	return;
        // }

        this.sellersService.register(dto).subscribe(
            (res: any) => {
                this.registerResponse = res;
            },
            err => {
                console.log(err.error);
            }
        );
    }

    continueRegistration(pm) {
        this.activePm = pm;
		this.loadComponent(pm.id);
    }

    onEmitBTC($event) {
        console.log("gerwe");
        if ($event) {
            this.showBTCForm = false;
        }

        this.registerResponse.paymentMethods.forEach(pm => {
            if (pm.id === 3) {
                pm.registerSuccess = true;
            }
        });
    }

    onEmitBank($event) {
        if ($event) {
            this.showBankForm = false;
        }

        this.registerResponse.paymentMethods.forEach(pm => {
            if (pm.id === 1) {
                pm.registerSuccess = true;
            }
        });
    }

    onEmitPayPal($event) {
        if ($event) {
            this.showPPForm = false;
        }

        this.registerResponse.paymentMethods.forEach(pm => {
            if (pm.id === 2) {
                pm.registerSuccess = true;
            }
        });
    }

    loadComponent(pmId: number) {

		const componentForLoad = this.getComponentForLoad(pmId);
		if (!componentForLoad) {
			return;
		}
		const componentFactory = this.componentFactoryResolver.resolveComponentFactory(componentForLoad.component);

		const viewContainerRef = this.appPm.viewContainerRef;
		viewContainerRef.clear();
		const componentRef = viewContainerRef.createComponent(componentFactory);

		(<IRegistrationComponent> componentRef.instance).sellerId = this.registerResponse.id;
		(<IRegistrationComponent> componentRef.instance).registrationLink = this.activePm.registrationLink;
		const subscription: Subscription = (<IRegistrationComponent> componentRef.instance).output.subscribe(
			event => {
				if (event) {
					componentRef.destroy();
					viewContainerRef.clear();

					this.fetchSeller();
				}
			}
		);

		componentRef.onDestroy(() => {
			subscription.unsubscribe();
		})
	}
	
	getComponentForLoad(pmId: number) {
		let retVal = null;
		this.pmService.pmRegistrationComponents.forEach(
            (pmRC: PmComponent) => {
				if (pmRC.id == pmId) {
					retVal = pmRC;
				}
			}
		);
		
		return retVal;
	}

	
	@ViewChild(PmDirective, {static: true}) appPm: PmDirective;


	
}
