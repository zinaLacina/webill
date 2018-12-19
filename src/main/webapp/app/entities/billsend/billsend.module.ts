import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WebillSharedModule } from 'app/shared';
import { PdfViewerModule } from 'ng2-pdf-viewer';
import {
    BillsendComponent,
    BillsendDetailComponent,
    BillsendUpdateComponent,
    BillsendDeletePopupComponent,
    BillsendDeleteDialogComponent,
    billsendRoute,
    billsendPopupRoute
} from './';

const ENTITY_STATES = [...billsendRoute, ...billsendPopupRoute];

@NgModule({
    imports: [WebillSharedModule, PdfViewerModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        BillsendComponent,
        BillsendDetailComponent,
        BillsendUpdateComponent,
        BillsendDeleteDialogComponent,
        BillsendDeletePopupComponent
    ],
    entryComponents: [BillsendComponent, BillsendUpdateComponent, BillsendDeleteDialogComponent, BillsendDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WebillBillsendModule {}
