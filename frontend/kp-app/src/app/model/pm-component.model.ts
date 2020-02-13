import { Type } from '@angular/core';

export class PmComponent {
  constructor(public component: Type<any>, public id: number = -1) {}
}
