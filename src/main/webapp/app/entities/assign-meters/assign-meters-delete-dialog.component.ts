import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAssignMeters } from 'app/shared/model/assign-meters.model';
import { AssignMetersService } from './assign-meters.service';

@Component({
    selector: 'jhi-assign-meters-delete-dialog',
    templateUrl: './assign-meters-delete-dialog.component.html'
})
export class AssignMetersDeleteDialogComponent {
    assignMeters: IAssignMeters;

    constructor(
        private assignMetersService: AssignMetersService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.assignMetersService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'assignMetersListModification',
                content: 'Deleted an assignMeters'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-assign-meters-delete-popup',
    template: ''
})
export class AssignMetersDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ assignMeters }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AssignMetersDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.assignMeters = assignMeters;
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
