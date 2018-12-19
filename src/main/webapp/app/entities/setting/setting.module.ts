import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WebillSharedModule } from 'app/shared';
import {
    SettingComponent,
    SettingDetailComponent,
    SettingUpdateComponent,
    SettingDeletePopupComponent,
    SettingDeleteDialogComponent,
    settingRoute,
    settingPopupRoute
} from './';

const ENTITY_STATES = [...settingRoute, ...settingPopupRoute];

@NgModule({
    imports: [WebillSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SettingComponent,
        SettingDetailComponent,
        SettingUpdateComponent,
        SettingDeleteDialogComponent,
        SettingDeletePopupComponent
    ],
    entryComponents: [SettingComponent, SettingUpdateComponent, SettingDeleteDialogComponent, SettingDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WebillSettingModule {}
