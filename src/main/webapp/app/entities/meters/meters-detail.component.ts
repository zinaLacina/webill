import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IMeters } from 'app/shared/model/meters.model';

@Component({
    selector: 'jhi-meters-detail',
    templateUrl: './meters-detail.component.html'
})
export class MetersDetailComponent implements OnInit {
    meters: IMeters;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ meters }) => {
            this.meters = meters;
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
