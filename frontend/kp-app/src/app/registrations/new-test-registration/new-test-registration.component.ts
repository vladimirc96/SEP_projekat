import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { IRegistrationComponent } from 'src/app/interfaces/i-registration.component';
import { Validators, FormBuilder } from '@angular/forms';
import { SellersService } from 'src/app/services/sellers.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-new-test-registration',
  templateUrl: './new-test-registration.component.html',
  styleUrls: ['./new-test-registration.component.css']
})
export class NewTestRegistrationComponent implements OnInit, IRegistrationComponent {

  @Input() sellerId: any;
  @Input() registrationLink: any;

  @Output() output: EventEmitter<boolean> = new EventEmitter();

  ntsRegistrationForm = this.fb.group({
    password: ["", Validators.required],
    someToken: ["", Validators.required]
  });
  
  constructor(private sellersService: SellersService, private fb: FormBuilder, private http: HttpClient) {

  }

  ngOnInit() {
  }

  onSubmit() {
      let dto = {
          id: this.sellerId,
          password: this.ntsRegistrationForm.get('password').value,
          someToken: this.ntsRegistrationForm.get('someToken').value
      }

      this.registerNewTestService(dto);
  }

  registerNewTestService(dto: any) {

      this.http.post(this.registrationLink, dto).subscribe(
          (res: any) => {
              this.output.emit(true);
          }, error => console.log(error.error)
      )
  }

}
