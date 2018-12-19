/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { WebillTestModule } from '../../../test.module';
import { NotificationsDeleteDialogComponent } from 'app/entities/notifications/notifications-delete-dialog.component';
import { NotificationsService } from 'app/entities/notifications/notifications.service';

describe('Component Tests', () => {
    describe('Notifications Management Delete Component', () => {
        let comp: NotificationsDeleteDialogComponent;
        let fixture: ComponentFixture<NotificationsDeleteDialogComponent>;
        let service: NotificationsService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WebillTestModule],
                declarations: [NotificationsDeleteDialogComponent]
            })
                .overrideTemplate(NotificationsDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(NotificationsDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NotificationsService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
