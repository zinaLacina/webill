import { NgModule } from '@angular/core';

import { WebillSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [WebillSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [WebillSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class WebillSharedCommonModule {}
