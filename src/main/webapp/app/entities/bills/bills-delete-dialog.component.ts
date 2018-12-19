import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBills } from 'app/shared/model/bills.model';
import { BillsService } from './bills.service';

@Component({
    selector: 'jhi-bills-delete-dialog',
    templateUrl: './bills-delete-dialog.component.html'
})
export class BillsDeleteDialogComponent {
    bills: IBills;

    constructor(private billsService: BillsService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.billsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'billsListModification',
                content: 'Deleted an bills'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-bills-delete-popup',
    template: ''
})
export class BillsDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ bills }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BillsDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.bills = bills;
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
