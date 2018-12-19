import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBillSetting } from 'app/shared/model/bill-setting.model';

@Component({
    selector: 'jhi-bill-setting-detail',
    templateUrl: './bill-setting-detail.component.html'
})
export class BillSettingDetailComponent implements OnInit {
    billSetting: IBillSetting;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ billSetting }) => {
            this.billSetting = billSetting;
        });
    }

    previousState() {
        window.history.back();
    }
}
