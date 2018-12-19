import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WebillSharedModule } from 'app/shared';
import {
    BillSettingComponent,
    BillSettingDetailComponent,
    BillSettingUpdateComponent,
    BillSettingDeletePopupComponent,
    BillSettingDeleteDialogComponent,
    billSettingRoute,
    billSettingPopupRoute
} from './';

const ENTITY_STATES = [...billSettingRoute, ...billSettingPopupRoute];

@NgModule({
    imports: [WebillSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        BillSettingComponent,
        BillSettingDetailComponent,
        BillSettingUpdateComponent,
        BillSettingDeleteDialogComponent,
        BillSettingDeletePopupComponent
    ],
    entryComponents: [BillSettingComponent, BillSettingUpdateComponent, BillSettingDeleteDialogComponent, BillSettingDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WebillBillSettingModule {}
