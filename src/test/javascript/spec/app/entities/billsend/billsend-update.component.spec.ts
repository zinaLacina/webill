/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { WebillTestModule } from '../../../test.module';
import { BillsendUpdateComponent } from 'app/entities/billsend/billsend-update.component';
import { BillsendService } from 'app/entities/billsend/billsend.service';
import { Billsend } from 'app/shared/model/billsend.model';

describe('Component Tests', () => {
    describe('Billsend Management Update Component', () => {
        let comp: BillsendUpdateComponent;
        let fixture: ComponentFixture<BillsendUpdateComponent>;
        let service: BillsendService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WebillTestModule],
                declarations: [BillsendUpdateComponent]
            })
                .overrideTemplate(BillsendUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(BillsendUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BillsendService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Billsend(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.billsend = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Billsend();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.billsend = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
