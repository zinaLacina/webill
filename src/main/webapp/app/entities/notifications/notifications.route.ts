import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Notifications } from 'app/shared/model/notifications.model';
import { NotificationsService } from './notifications.service';
import { NotificationsComponent } from './notifications.component';
import { NotificationsDetailComponent } from './notifications-detail.component';
import { NotificationsUpdateComponent } from './notifications-update.component';
import { NotificationsDeletePopupComponent } from './notifications-delete-dialog.component';
import { INotifications } from 'app/shared/model/notifications.model';

@Injectable({ providedIn: 'root' })
export class NotificationsResolve implements Resolve<INotifications> {
    constructor(private service: NotificationsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((notifications: HttpResponse<Notifications>) => notifications.body));
        }
        return of(new Notifications());
    }
}

export const notificationsRoute: Routes = [
    {
        path: 'notifications',
        component: NotificationsComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER','ROLE_CONSUMER'],
            defaultSort: 'id,asc',
            pageTitle: 'Notifications'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'notifications/:id/view',
        component: NotificationsDetailComponent,
        resolve: {
            notifications: NotificationsResolve
        },
        data: {
            authorities: ['ROLE_USER','ROLE_CONSUMER'],
            pageTitle: 'Notifications'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'notifications/new',
        component: NotificationsUpdateComponent,
        resolve: {
            notifications: NotificationsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Notifications'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'notifications/:id/edit',
        component: NotificationsUpdateComponent,
        resolve: {
            notifications: NotificationsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Notifications'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const notificationsPopupRoute: Routes = [
    {
        path: 'notifications/:id/delete',
        component: NotificationsDeletePopupComponent,
        resolve: {
            notifications: NotificationsResolve
        },
        data: {
            authorities: ['ROLE_USER','ROLE_CONSUMER'],
            pageTitle: 'Notifications'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
