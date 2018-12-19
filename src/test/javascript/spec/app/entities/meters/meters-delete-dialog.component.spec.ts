/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { WebillTestModule } from '../../../test.module';
import { MetersDeleteDialogComponent } from 'app/entities/meters/meters-delete-dialog.component';
import { MetersService } from 'app/entities/meters/meters.service';

describe('Component Tests', () => {
    describe('Meters Management Delete Component', () => {
        let comp: MetersDeleteDialogComponent;
        let fixture: ComponentFixture<MetersDeleteDialogComponent>;
        let service: MetersService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WebillTestModule],
                declarations: [MetersDeleteDialogComponent]
            })
                .overrideTemplate(MetersDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(MetersDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MetersService);
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
