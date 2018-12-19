import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Billsend } from 'app/shared/model/billsend.model';
import { BillsendService } from './billsend.service';
import { BillsendComponent } from './billsend.component';
import { BillsendDetailComponent } from './billsend-detail.component';
import { BillsendUpdateComponent } from './billsend-update.component';
import { BillsendDeletePopupComponent } from './billsend-delete-dialog.component';
import { IBillsend } from 'app/shared/model/billsend.model';

@Injectable({ providedIn: 'root' })
export class BillsendResolve implements Resolve<IBillsend> {
    constructor(private service: BillsendService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((billsend: HttpResponse<Billsend>) => billsend.body));
        }
        return of(new Billsend());
    }
}

export const billsendRoute: Routes = [
    {
        path: 'billsend',
        component: BillsendComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER','ROLE_CONSUMER'],
            defaultSort: 'id,asc',
            pageTitle: 'Billsends'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'billsend/:id/view',
        component: BillsendDetailComponent,
        resolve: {
            billsend: BillsendResolve
        },
        data: {
            authorities: ['ROLE_USER','ROLE_CONSUMER'],
            pageTitle: 'Billsends'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'billsend/new',
        component: BillsendUpdateComponent,
        resolve: {
            billsend: BillsendResolve
        },
        data: {
            authorities: ['ROLE_USER','ROLE_CONSUMER'],
            pageTitle: 'Billsends'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'billsend/:id/edit',
        component: BillsendUpdateComponent,
        resolve: {
            billsend: BillsendResolve
        },
        data: {
            authorities: ['ROLE_USER','ROLE_CONSUMER'],
            pageTitle: 'Billsends'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const billsendPopupRoute: Routes = [
    {
        path: 'billsend/:id/delete',
        component: BillsendDeletePopupComponent,
        resolve: {
            billsend: BillsendResolve
        },
        data: {
            authorities: ['ROLE_USER','ROLE_CONSUMER'],
            pageTitle: 'Billsends'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
