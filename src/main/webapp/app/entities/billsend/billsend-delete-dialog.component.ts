import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBillsend } from 'app/shared/model/billsend.model';
import { BillsendService } from './billsend.service';
import {IBills} from "app/shared/model/bills.model";

@Component({
    selector: 'jhi-billsend-delete-dialog',
    templateUrl: './billsend-delete-dialog.component.html'
})
export class BillsendDeleteDialogComponent {
    billsend: IBills;

    constructor(private billsendService: BillsendService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.billsendService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'billsendListModification',
                content: 'Deleted an billsend'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-billsend-delete-popup',
    template: ''
})
export class BillsendDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ billsend }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BillsendDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.billsend = billsend;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
