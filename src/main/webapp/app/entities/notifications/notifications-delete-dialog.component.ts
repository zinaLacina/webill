import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { INotifications } from 'app/shared/model/notifications.model';
import { NotificationsService } from './notifications.service';

@Component({
    selector: 'jhi-notifications-delete-dialog',
    templateUrl: './notifications-delete-dialog.component.html'
})
export class NotificationsDeleteDialogComponent {
    notifications: INotifications;

    constructor(
        private notificationsService: NotificationsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.notificationsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'notificationsListModification',
                content: 'Deleted an notifications'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-notifications-delete-popup',
    template: ''
})
export class NotificationsDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ notifications }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(NotificationsDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.notifications = notifications;
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
