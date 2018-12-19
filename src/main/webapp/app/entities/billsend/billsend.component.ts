import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IBillsend } from 'app/shared/model/billsend.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { BillsendService } from './billsend.service';
import {IBills} from "app/shared/model/bills.model";

@Component({
    selector: 'jhi-billsend',
    templateUrl: './billsend.component.html'
})
export class BillsendComponent implements OnInit, OnDestroy {
    currentAccount: any;
    billsends: IBills[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    constructor(
        private billsendService: BillsendService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    loadAll() {
        this.billsendService
            .findMyBills()
            .subscribe(
                (res: HttpResponse<IBillsend[]>) => this.billsendsM(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/billsend'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/billsend',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInBillsends();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IBillsend) {
        return item.id;
    }

    registerChangeInBillsends() {
        this.eventSubscriber = this.eventManager.subscribe('billsendListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateBillsends(data: IBills[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.billsends = data;
    }

    private billsendsM(data: IBills[], headers: HttpHeaders) {
        this.billsends = data;
    }


    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
