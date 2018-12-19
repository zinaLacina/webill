import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IBills } from 'app/shared/model/bills.model';

@Component({
    selector: 'jhi-bills-detail',
    templateUrl: './bills-detail.component.html'
})
export class BillsDetailComponent implements OnInit {
    bills: IBills;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ bills }) => {
            this.bills = bills;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}
