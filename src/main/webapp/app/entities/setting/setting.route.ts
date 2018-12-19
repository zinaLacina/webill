import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Setting } from 'app/shared/model/setting.model';
import { SettingService } from './setting.service';
import { SettingComponent } from './setting.component';
import { SettingDetailComponent } from './setting-detail.component';
import { SettingUpdateComponent } from './setting-update.component';
import { SettingDeletePopupComponent } from './setting-delete-dialog.component';
import { ISetting } from 'app/shared/model/setting.model';

@Injectable({ providedIn: 'root' })
export class SettingResolve implements Resolve<ISetting> {
    constructor(private service: SettingService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((setting: HttpResponse<Setting>) => setting.body));
        }
        return of(new Setting());
    }
}

export const settingRoute: Routes = [
    {
        path: 'setting',
        component: SettingComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Settings'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'setting/:id/view',
        component: SettingDetailComponent,
        resolve: {
            setting: SettingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Settings'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'setting/new',
        component: SettingUpdateComponent,
        resolve: {
            setting: SettingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Settings'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'setting/:id/edit',
        component: SettingUpdateComponent,
        resolve: {
            setting: SettingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Settings'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const settingPopupRoute: Routes = [
    {
        path: 'setting/:id/delete',
        component: SettingDeletePopupComponent,
        resolve: {
            setting: SettingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Settings'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
