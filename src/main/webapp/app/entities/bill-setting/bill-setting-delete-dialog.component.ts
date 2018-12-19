import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBillSetting } from 'app/shared/model/bill-setting.model';
import { BillSettingService } from './bill-setting.service';

@Component({
    selector: 'jhi-bill-setting-delete-dialog',
    templateUrl: './bill-setting-delete-dialog.component.html'
})
export class BillSettingDeleteDialogComponent {
    billSetting: IBillSetting;

    constructor(
        private billSettingService: BillSettingService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.billSettingService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'billSettingListModification',
                content: 'Deleted an billSetting'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-bill-setting-delete-popup',
    template: ''
})
export class BillSettingDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ billSetting }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BillSettingDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.billSetting = billSetting;
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
