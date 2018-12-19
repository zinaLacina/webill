import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WebillSharedModule } from 'app/shared';
import { WebillAdminModule } from 'app/admin/admin.module';
import {
    AssignMetersComponent,
    AssignMetersDetailComponent,
    AssignMetersUpdateComponent,
    AssignMetersDeletePopupComponent,
    AssignMetersDeleteDialogComponent,
    assignMetersRoute,
    assignMetersPopupRoute
} from './';

const ENTITY_STATES = [...assignMetersRoute, ...assignMetersPopupRoute];

@NgModule({
    imports: [WebillSharedModule, WebillAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AssignMetersComponent,
        AssignMetersDetailComponent,
        AssignMetersUpdateComponent,
        AssignMetersDeleteDialogComponent,
        AssignMetersDeletePopupComponent
    ],
    entryComponents: [
        AssignMetersComponent,
        AssignMetersUpdateComponent,
        AssignMetersDeleteDialogComponent,
        AssignMetersDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WebillAssignMetersModule {}
