import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WebillSharedModule } from 'app/shared';
import { WebillAdminModule } from 'app/admin/admin.module';
import {
    NotificationsComponent,
    NotificationsDetailComponent,
    NotificationsUpdateComponent,
    NotificationsDeletePopupComponent,
    NotificationsDeleteDialogComponent,
    notificationsRoute,
    notificationsPopupRoute
} from './';

const ENTITY_STATES = [...notificationsRoute, ...notificationsPopupRoute];

@NgModule({
    imports: [WebillSharedModule, WebillAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        NotificationsComponent,
        NotificationsDetailComponent,
        NotificationsUpdateComponent,
        NotificationsDeleteDialogComponent,
        NotificationsDeletePopupComponent
    ],
    entryComponents: [
        NotificationsComponent,
        NotificationsUpdateComponent,
        NotificationsDeleteDialogComponent,
        NotificationsDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WebillNotificationsModule {}
