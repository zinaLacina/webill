import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ISetting } from 'app/shared/model/setting.model';

@Component({
    selector: 'jhi-setting-detail',
    templateUrl: './setting-detail.component.html'
})
export class SettingDetailComponent implements OnInit {
    setting: ISetting;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ setting }) => {
            this.setting = setting;
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
