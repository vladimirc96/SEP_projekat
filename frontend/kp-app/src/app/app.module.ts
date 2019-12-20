import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { SellersComponent } from './sellers/sellers.component';
import { AppRoutingModule } from './app-routing.module';
import { SellersService } from './services/sellers.service';
import { PaypalService } from './services/paypal.service';
import { PaypalComponent } from './paypal/paypal.component';

@NgModule({
  declarations: [
    AppComponent,
    SellersComponent,
    PaypalComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [SellersService, PaypalService],
  bootstrap: [AppComponent]
})
export class AppModule { }
