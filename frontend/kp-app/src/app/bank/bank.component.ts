import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormControlDirective } from '@angular/forms';
import { BankService } from '../services/bank.service';
import { CentralaService } from '../services/centrala.service';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-bank',
  templateUrl: './bank.component.html',
  styleUrls: ['./bank.component.css']
})
export class BankComponent implements OnInit {

  constructor() { 
  }

  ngOnInit() {
  }


}
