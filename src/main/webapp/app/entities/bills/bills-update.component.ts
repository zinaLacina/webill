import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IBills } from 'app/shared/model/bills.model';
import { BillsService } from './bills.service';
import { IUser, UserService } from 'app/core';
import { IAssignMeters } from 'app/shared/model/assign-meters.model';
import { AssignMetersService } from 'app/entities/assign-meters';
import { IBillSetting } from 'app/shared/model/bill-setting.model';
import { BillSettingService } from 'app/entities/bill-setting';

@Component({
    selector: 'jhi-bills-update',
    templateUrl: './bills-update.component.html'
})
export class BillsUpdateComponent implements OnInit {
    bills: IBills;
    isSaving: boolean;

    users: IUser[];

    assignmeters: IAssignMeters[];

    billsettings: IBillSetting[];
    deadline: string;
    dateCreated: string;
    dateModified: string;

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private billsService: BillsService,
        private userService: UserService,
        private assignMetersService: AssignMetersService,
        private billSettingService: BillSettingService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ bills }) => {
            this.bills = bills;
            this.deadline = this.bills.deadline != null ? this.bills.deadline.format(DATE_TIME_FORMAT) : null;
            this.dateCreated = this.bills.dateCreated != null ? this.bills.dateCreated.format(DATE_TIME_FORMAT) : null;
            this.dateModified = this.bills.dateModified != null ? this.bills.dateModified.format(DATE_TIME_FORMAT) : null;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.assignMetersService.query().subscribe(
            (res: HttpResponse<IAssignMeters[]>) => {
                this.assignmeters = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.billSettingService.query().subscribe(
            (res: HttpResponse<IBillSetting[]>) => {
                this.billsettings = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.bills.deadline = this.deadline != null ? moment(this.deadline, DATE_TIME_FORMAT) : null;
        this.bills.dateCreated = this.dateCreated != null ? moment(this.dateCreated, DATE_TIME_FORMAT) : null;
        this.bills.dateModified = this.dateModified != null ? moment(this.dateModified, DATE_TIME_FORMAT) : null;
        if (this.bills.id !== undefined) {
            this.subscribeToSaveResponse(this.billsService.update(this.bills));
        } else {
            this.subscribeToSaveResponse(this.billsService.create(this.bills));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IBills>>) {
        result.subscribe((res: HttpResponse<IBills>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackAssignMetersById(index: number, item: IAssignMeters) {
        return item.id;
    }

    trackBillSettingById(index: number, item: IBillSetting) {
        return item.id;
    }
}
