import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import * as _ from 'lodash';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IAssignMeters } from 'app/shared/model/assign-meters.model';
import { AssignMetersService } from './assign-meters.service';
import { IUser, UserService } from 'app/core';
import { IMeters } from 'app/shared/model/meters.model';
import { MetersService } from 'app/entities/meters';

@Component({
    selector: 'jhi-assign-meters-update',
    templateUrl: './assign-meters-update.component.html'
})
export class AssignMetersUpdateComponent implements OnInit {
    assignMeters: IAssignMeters;
    isSaving: boolean;

    users: IUser[];
    consumers: IUser[];
    test:any;

    meters: IMeters[];
    createdAt: string;
    updateAt: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private assignMetersService: AssignMetersService,
        private userService: UserService,
        private metersService: MetersService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ assignMeters }) => {
            this.assignMeters = assignMeters;
            this.createdAt = this.assignMeters.createdAt != null ? this.assignMeters.createdAt.format(DATE_TIME_FORMAT) : null;
            this.updateAt = this.assignMeters.updateAt != null ? this.assignMeters.updateAt.format(DATE_TIME_FORMAT) : null;
        });


        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;

                // this.test = _.find(this.users,function (o) {
                //     return o.authorities[0]="ROLE_CONSUMER";
                // });
                //
                // console.log(this.test);
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.metersService.query().subscribe(
            (res: HttpResponse<IMeters[]>) => {
                this.meters = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.assignMeters.createdAt = this.createdAt != null ? moment(this.createdAt, DATE_TIME_FORMAT) : null;
        this.assignMeters.updateAt = this.updateAt != null ? moment(this.updateAt, DATE_TIME_FORMAT) : null;
        if (this.assignMeters.id !== undefined) {
            this.subscribeToSaveResponse(this.assignMetersService.update(this.assignMeters));
        } else {
            this.subscribeToSaveResponse(this.assignMetersService.create(this.assignMeters));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IAssignMeters>>) {
        result.subscribe((res: HttpResponse<IAssignMeters>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackMetersById(index: number, item: IMeters) {
        return item.id;
    }
}
