import { HttpClient } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { Router } from '@angular/router';
import { ReturnStatement } from '@angular/compiler';


@Injectable()
export class SellersService {

    private ENDPOINT_URI: string = "api/sellers";

    constructor(private http: HttpClient, private router: Router) { }

    getSeller(id: number) { 
        return this.http.get(this.ENDPOINT_URI + "/sellers/" + id);
    }

}