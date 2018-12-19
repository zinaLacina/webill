import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Meters } from 'app/shared/model/meters.model';
import { MetersService } from './meters.service';
import { MetersComponent } from './meters.component';
import { MetersDetailComponent } from './meters-detail.component';
import { MetersUpdateComponent } from './meters-update.component';
import { MetersDeletePopupComponent } from './meters-delete-dialog.component';
import { IMeters } from 'app/shared/model/meters.model';

@Injectable({ providedIn: 'root' })
export class MetersResolve implements Resolve<IMeters> {
    constructor(private service: MetersService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((meters: HttpResponse<Meters>) => meters.body));
        }
        return of(new Meters());
    }
}

export const metersRoute: Routes = [
    {
        path: 'meters',
        component: MetersComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Meters'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'meters/:id/view',
        component: MetersDetailComponent,
        resolve: {
            meters: MetersResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Meters'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'meters/new',
        component: MetersUpdateComponent,
        resolve: {
            meters: MetersResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Meters'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'meters/:id/edit',
        component: MetersUpdateComponent,
        resolve: {
            meters: MetersResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Meters'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const metersPopupRoute: Routes = [
    {
        path: 'meters/:id/delete',
        component: MetersDeletePopupComponent,
        resolve: {
            meters: MetersResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Meters'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
