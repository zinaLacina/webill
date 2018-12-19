import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WebillSharedModule } from "app/shared";
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';

import {
    MetersComponent,
    MetersDetailComponent,
    MetersUpdateComponent,
    MetersDeletePopupComponent,
    MetersDeleteDialogComponent,
    metersRoute,
    metersPopupRoute
} from './';
import {AgmCoreModule} from "@agm/core";

const ENTITY_STATES = [...metersRoute, ...metersPopupRoute];

@NgModule({
    imports: [WebillSharedModule,
        NgbModule,
        AgmCoreModule.forRoot({
            apiKey: 'AIzaSyDhVI0zjjJajI__6Kz2e-HvBJ4dojW3mWw'
        }),
        RouterModule.forChild(ENTITY_STATES)],
    declarations: [MetersComponent, MetersDetailComponent, MetersUpdateComponent, MetersDeleteDialogComponent, MetersDeletePopupComponent],
    entryComponents: [MetersComponent, MetersUpdateComponent, MetersDeleteDialogComponent, MetersDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WebillMetersModule {}
