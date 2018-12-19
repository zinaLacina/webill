import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { AssignMeters } from 'app/shared/model/assign-meters.model';
import { AssignMetersService } from './assign-meters.service';
import { AssignMetersComponent } from './assign-meters.component';
import { AssignMetersDetailComponent } from './assign-meters-detail.component';
import { AssignMetersUpdateComponent } from './assign-meters-update.component';
import { AssignMetersDeletePopupComponent } from './assign-meters-delete-dialog.component';
import { IAssignMeters } from 'app/shared/model/assign-meters.model';

@Injectable({ providedIn: 'root' })
export class AssignMetersResolve implements Resolve<IAssignMeters> {
    constructor(private service: AssignMetersService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((assignMeters: HttpResponse<AssignMeters>) => assignMeters.body));
        }
        return of(new AssignMeters());
    }
}

export const assignMetersRoute: Routes = [
    {
        path: 'assign-meters',
        component: AssignMetersComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'AssignMeters'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'assign-meters/:id/view',
        component: AssignMetersDetailComponent,
        resolve: {
            assignMeters: AssignMetersResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AssignMeters'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'assign-meters/new',
        component: AssignMetersUpdateComponent,
        resolve: {
            assignMeters: AssignMetersResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AssignMeters'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'assign-meters/:id/edit',
        component: AssignMetersUpdateComponent,
        resolve: {
            assignMeters: AssignMetersResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AssignMeters'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const assignMetersPopupRoute: Routes = [
    {
        path: 'assign-meters/:id/delete',
        component: AssignMetersDeletePopupComponent,
        resolve: {
            assignMeters: AssignMetersResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AssignMeters'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
