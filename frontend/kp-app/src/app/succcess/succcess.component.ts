import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-succcess',
  templateUrl: './succcess.component.html',
  styleUrls: ['./succcess.component.css']
})
export class SucccessComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  goHome() {
    window.location.href = "https://localhost:4200/centrala";
  }


}
