import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { INotifications } from 'app/shared/model/notifications.model';
import { NotificationsService } from './notifications.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-notifications-update',
    templateUrl: './notifications-update.component.html'
})
export class NotificationsUpdateComponent implements OnInit {
    notifications: INotifications;
    isSaving: boolean;

    users: IUser[];
    createdAt: string;
    updateAt: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private notificationsService: NotificationsService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ notifications }) => {
            this.notifications = notifications;
            this.createdAt = this.notifications.createdAt != null ? this.notifications.createdAt.format(DATE_TIME_FORMAT) : null;
            this.updateAt = this.notifications.updateAt != null ? this.notifications.updateAt.format(DATE_TIME_FORMAT) : null;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.notifications.createdAt = this.createdAt != null ? moment(this.createdAt, DATE_TIME_FORMAT) : null;
        this.notifications.updateAt = this.updateAt != null ? moment(this.updateAt, DATE_TIME_FORMAT) : null;
        if (this.notifications.id !== undefined) {
            this.subscribeToSaveResponse(this.notificationsService.update(this.notifications));
        } else {
            this.subscribeToSaveResponse(this.notificationsService.create(this.notifications));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<INotifications>>) {
        result.subscribe((res: HttpResponse<INotifications>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
}
