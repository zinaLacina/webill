import { Route } from '@angular/router';

import { HomeComponent } from './';
import { AboutComponent } from 'app/about/about.component';
import { HistoryComponent } from 'app/history/history.component';
import {NoticeComponent} from "app/notice/notice.component";

export const HOME_ROUTE: Route = {
    path: '',
    component: HomeComponent,
    data: {
        authorities: [],
        pageTitle: 'Welcome to weBill'
    }
};

export const ABOUT_ROUTE: Route = {
    path: 'about',
    component: AboutComponent,
    data: {
        authorities: [],
        pageTitle: 'About'
    }
};

export const HISTORY_ROUTE: Route = {
    path: 'history',
    component: HistoryComponent,
    data: {
        authorities: [],
        pageTitle: 'History'
    }
};

export const NOTICE_ROUTE: Route = {
    path: 'notice',
    component: NoticeComponent,
    data: {
        authorities: [],
        pageTitle: 'Notifications'
    }
};

