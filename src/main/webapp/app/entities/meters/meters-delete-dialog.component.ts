import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMeters } from 'app/shared/model/meters.model';
import { MetersService } from './meters.service';

@Component({
    selector: 'jhi-meters-delete-dialog',
    templateUrl: './meters-delete-dialog.component.html'
})
export class MetersDeleteDialogComponent {
    meters: IMeters;

    constructor(private metersService: MetersService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.metersService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'metersListModification',
                content: 'Deleted an meters'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-meters-delete-popup',
    template: ''
})
export class MetersDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ meters }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MetersDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.meters = meters;
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
