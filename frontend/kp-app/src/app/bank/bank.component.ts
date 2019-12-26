import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormControlDirective } from '@angular/forms';
import { BankService } from '../services/bank.service';

@Component({
  selector: 'app-bank',
  templateUrl: './bank.component.html',
  styleUrls: ['./bank.component.css']
})
export class BankComponent implements OnInit {

  infoForm = new FormGroup({
    pan: new FormControl(''),
    securityCode: new FormControl(''),
    cardholderName: new FormControl(''),
    expirationDate: new FormControl('')
  })

  constructor(private bankService: BankService) { }

  ngOnInit() {
  }

}
