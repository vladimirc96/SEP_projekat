import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ActiveOrderService {

  constructor(private httpClient: HttpClient) { }

  
  getActiveOrder(id){
    return this.httpClient.get('/api/sellers/active-order/' + id);
  }

}
