import { Component, OnInit } from '@angular/core';
import { CentralaService } from '../services/centrala.service';

@Component({
  selector: 'app-bitcoin',
  templateUrl: './bitcoin.component.html',
  styleUrls: ['./bitcoin.component.css']
})
export class BitcoinComponent implements OnInit {

  rad: any = null;
  constructor(private centralaService: CentralaService) { 
    this.rad = this.centralaService.activeRad;
  }

  ngOnInit() {
  }

}
