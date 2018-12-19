import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'jhi-footer',
    templateUrl: './footer.component.html'
})
export class FooterComponent  implements OnInit{
    data: Date = new Date();

    constructor() { }

    ngOnInit() {
    }
}
