import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import {IBills} from "app/shared/model/bills.model";

@Component({
    selector: 'jhi-billsend-detail',
    templateUrl: './billsend-detail.component.html'
})
export class BillsendDetailComponent implements OnInit {
    billsend: IBills;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ billsend }) => {
            this.billsend = billsend;
        });
    }

    previousState() {
        window.history.back();
    }
}
