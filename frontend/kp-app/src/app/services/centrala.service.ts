import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CentralaService {

  radovi: any[] = [
		{
			title: "Humanizacija razvoja softvera",
			author: "Dzon Dou",
			abstract: "Lorem ipsum, dolor sit amet consectetur adipisicing elit. Cupiditate aspernatur esse cum ratione soluta.",
			sellerId: 1,
			price: 7
		},
		{
			title: "Psihoza stanovništva",
			author: "Zonto Make",
			abstract: "Psam reprehenderit quia aperiam totam corrupti animi, similique laborum, nam vero saepe nihil a eos!",
			sellerId: 2,
			price: 4
		},
		{
			title: "Psihologija čoveka",
			author: "Dzon Dou",
			abstract: "Lorem ipsum, dolor sit amet consectetur adipisicing elit. Cupiditate aspernatur esse cum ratione soluta.",
			sellerId: 1,
			price: 8000
		},
		{
			title: "Francuska revolucija",
			author: "Zonto Make",
			abstract: "Psam reprehenderit quia aperiam totam corrupti animi, similique laborum, nam vero saepe nihil a eos!",
			sellerId: 1,
			price: 12000
		}

  ]
  
  activeRad: any = null;

  constructor() { }
}
