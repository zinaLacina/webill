import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INotifications } from 'app/shared/model/notifications.model';
import {NotificationsService} from "app/entities/notifications/notifications.service";

@Component({
    selector: 'jhi-notifications-detail',
    templateUrl: './notifications-detail.component.html'
})
export class NotificationsDetailComponent implements OnInit {
    notifications: INotifications;

    constructor(private activatedRoute: ActivatedRoute,private notificationsService: NotificationsService,) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ notifications }) => {
            this.notifications = notifications;
        });
    }

    previousState() {
        //this.notificationsService.checked(this.notifications.id);
        window.history.back();
    }
}
