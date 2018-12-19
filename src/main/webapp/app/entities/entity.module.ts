import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { WebillSettingModule } from './setting/setting.module';
import { WebillBillSettingModule } from './bill-setting/bill-setting.module';
import { WebillMetersModule } from './meters/meters.module';
import { WebillNotificationsModule } from './notifications/notifications.module';
import { WebillAssignMetersModule } from './assign-meters/assign-meters.module';
import { WebillBillsModule } from './bills/bills.module';
import { WebillBillsendModule } from './billsend/billsend.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        WebillSettingModule,
        WebillBillSettingModule,
        WebillMetersModule,
        WebillNotificationsModule,
        WebillAssignMetersModule,
        WebillBillsModule,
        WebillBillsendModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WebillEntityModule {}
