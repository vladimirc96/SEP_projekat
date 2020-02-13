import { Directive, ViewContainerRef } from '@angular/core';

@Directive({
  selector: '[appPm]'
})
export class PmDirective {

  constructor(public viewContainerRef: ViewContainerRef) { }

}
