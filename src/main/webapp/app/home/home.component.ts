import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IAssignMeters } from 'app/shared/model/assign-meters.model';

import { LoginModalService, Principal, Account } from 'app/core';
import { AssignMetersService } from 'app/entities/assign-meters/assign-meters.service';
import {HttpErrorResponse, HttpHeaders, HttpResponse} from "@angular/common/http";
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    assignMeters: IAssignMeters;

    hasAffected: boolean = false;

    constructor(private principal: Principal,
                private jhiAlertService: JhiAlertService,
                private loginModalService: LoginModalService,
                private eventManager: JhiEventManager,
                private assignMetersService: AssignMetersService, ) {}

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
            if (this.isAuthenticated()) {
                this.getMeters();
            }
        });
        this.registerAuthenticationSuccess();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.principal.identity().then(account => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    //Get list of the meters of the user enable and disable
    getMeters(){
        this.assignMetersService.mymeters().subscribe((assignMeters: any) => {
            this.assignMeters = assignMeters.body;
            if(this.assignMeters.id!=null){
                this.hasAffected = true;
            }
            //console.log(this.assignMeters);
        });
    }

    sort() {
        return "id,desc";
    }


    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
