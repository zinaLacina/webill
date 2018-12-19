import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { BillSetting } from 'app/shared/model/bill-setting.model';
import { BillSettingService } from './bill-setting.service';
import { BillSettingComponent } from './bill-setting.component';
import { BillSettingDetailComponent } from './bill-setting-detail.component';
import { BillSettingUpdateComponent } from './bill-setting-update.component';
import { BillSettingDeletePopupComponent } from './bill-setting-delete-dialog.component';
import { IBillSetting } from 'app/shared/model/bill-setting.model';

@Injectable({ providedIn: 'root' })
export class BillSettingResolve implements Resolve<IBillSetting> {
    constructor(private service: BillSettingService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((billSetting: HttpResponse<BillSetting>) => billSetting.body));
        }
        return of(new BillSetting());
    }
}

export const billSettingRoute: Routes = [
    {
        path: 'bill-setting',
        component: BillSettingComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'BillSettings'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'bill-setting/:id/view',
        component: BillSettingDetailComponent,
        resolve: {
            billSetting: BillSettingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BillSettings'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'bill-setting/new',
        component: BillSettingUpdateComponent,
        resolve: {
            billSetting: BillSettingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BillSettings'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'bill-setting/:id/edit',
        component: BillSettingUpdateComponent,
        resolve: {
            billSetting: BillSettingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BillSettings'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const billSettingPopupRoute: Routes = [
    {
        path: 'bill-setting/:id/delete',
        component: BillSettingDeletePopupComponent,
        resolve: {
            billSetting: BillSettingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BillSettings'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
