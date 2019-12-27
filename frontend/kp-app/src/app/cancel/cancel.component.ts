import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-cancel',
  templateUrl: './cancel.component.html',
  styleUrls: ['./cancel.component.css']
})
export class CancelComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  
  goHome() {
    window.location.href = "https://localhost:4200/centrala";
  }


}
