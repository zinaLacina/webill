import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IBillSetting } from 'app/shared/model/bill-setting.model';
import { BillSettingService } from './bill-setting.service';

@Component({
    selector: 'jhi-bill-setting-update',
    templateUrl: './bill-setting-update.component.html'
})
export class BillSettingUpdateComponent implements OnInit {
    billSetting: IBillSetting;
    isSaving: boolean;
    dateCreated: string;
    dateModified: string;

    constructor(private billSettingService: BillSettingService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ billSetting }) => {
            this.billSetting = billSetting;
            this.dateCreated = this.billSetting.dateCreated != null ? this.billSetting.dateCreated.format(DATE_TIME_FORMAT) : null;
            this.dateModified = this.billSetting.dateModified != null ? this.billSetting.dateModified.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.billSetting.dateCreated = this.dateCreated != null ? moment(this.dateCreated, DATE_TIME_FORMAT) : null;
        this.billSetting.dateModified = this.dateModified != null ? moment(this.dateModified, DATE_TIME_FORMAT) : null;
        if (this.billSetting.id !== undefined) {
            this.subscribeToSaveResponse(this.billSettingService.update(this.billSetting));
        } else {
            this.subscribeToSaveResponse(this.billSettingService.create(this.billSetting));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IBillSetting>>) {
        result.subscribe((res: HttpResponse<IBillSetting>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
