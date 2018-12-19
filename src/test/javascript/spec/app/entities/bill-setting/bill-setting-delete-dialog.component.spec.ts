/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { WebillTestModule } from '../../../test.module';
import { BillSettingDeleteDialogComponent } from 'app/entities/bill-setting/bill-setting-delete-dialog.component';
import { BillSettingService } from 'app/entities/bill-setting/bill-setting.service';

describe('Component Tests', () => {
    describe('BillSetting Management Delete Component', () => {
        let comp: BillSettingDeleteDialogComponent;
        let fixture: ComponentFixture<BillSettingDeleteDialogComponent>;
        let service: BillSettingService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WebillTestModule],
                declarations: [BillSettingDeleteDialogComponent]
            })
                .overrideTemplate(BillSettingDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BillSettingDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BillSettingService);
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
