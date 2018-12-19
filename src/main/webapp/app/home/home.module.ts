import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NvD3Module } from 'ng2-nvd3';
import 'd3';
import 'nvd3';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { WebillSharedModule } from 'app/shared';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AboutComponent } from '../about/about.component';
import { HistoryComponent } from '../history/history.component';
import { NoticeComponent } from '../notice/notice.component';
import { HOME_ROUTE, HomeComponent, ABOUT_ROUTE, HISTORY_ROUTE, NOTICE_ROUTE } from './';

@NgModule({
    imports: [
        WebillSharedModule,
        NvD3Module,
        BrowserAnimationsModule,
        CalendarModule.forRoot({ provide: DateAdapter, useFactory: adapterFactory }),
        RouterModule.forChild([HOME_ROUTE, ABOUT_ROUTE, HISTORY_ROUTE, NOTICE_ROUTE])
    ],
    declarations: [HomeComponent, AboutComponent, HistoryComponent,NoticeComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WebillHomeModule {}
