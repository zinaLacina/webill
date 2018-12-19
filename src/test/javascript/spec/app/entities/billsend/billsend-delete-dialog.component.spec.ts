/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { WebillTestModule } from '../../../test.module';
import { BillsendDeleteDialogComponent } from 'app/entities/billsend/billsend-delete-dialog.component';
import { BillsendService } from 'app/entities/billsend/billsend.service';

describe('Component Tests', () => {
    describe('Billsend Management Delete Component', () => {
        let comp: BillsendDeleteDialogComponent;
        let fixture: ComponentFixture<BillsendDeleteDialogComponent>;
        let service: BillsendService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WebillTestModule],
                declarations: [BillsendDeleteDialogComponent]
            })
                .overrideTemplate(BillsendDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BillsendDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BillsendService);
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
