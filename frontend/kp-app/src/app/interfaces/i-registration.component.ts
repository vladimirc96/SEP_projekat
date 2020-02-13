import { EventEmitter } from '@angular/core';

export interface IRegistrationComponent {
    sellerId: number;
    registrationLink: string;
    output: EventEmitter<boolean>;
}
  