import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Bills } from 'app/shared/model/bills.model';
import { BillsService } from './bills.service';
import { BillsComponent } from './bills.component';
import { BillsDetailComponent } from './bills-detail.component';
import { BillsUpdateComponent } from './bills-update.component';
import { BillsDeletePopupComponent } from './bills-delete-dialog.component';
import { IBills } from 'app/shared/model/bills.model';

@Injectable({ providedIn: 'root' })
export class BillsResolve implements Resolve<IBills> {
    constructor(private service: BillsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((bills: HttpResponse<Bills>) => bills.body));
        }
        return of(new Bills());
    }
}

export const billsRoute: Routes = [
    {
        path: 'bills',
        component: BillsComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Bills'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'bills/:id/view',
        component: BillsDetailComponent,
        resolve: {
            bills: BillsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Bills'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'bills/new',
        component: BillsUpdateComponent,
        resolve: {
            bills: BillsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Bills'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'bills/:id/edit',
        component: BillsUpdateComponent,
        resolve: {
            bills: BillsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Bills'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const billsPopupRoute: Routes = [
    {
        path: 'bills/:id/delete',
        component: BillsDeletePopupComponent,
        resolve: {
            bills: BillsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Bills'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
