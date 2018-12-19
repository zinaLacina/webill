/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { WebillTestModule } from '../../../test.module';
import { MetersUpdateComponent } from 'app/entities/meters/meters-update.component';
import { MetersService } from 'app/entities/meters/meters.service';
import { Meters } from 'app/shared/model/meters.model';

describe('Component Tests', () => {
    describe('Meters Management Update Component', () => {
        let comp: MetersUpdateComponent;
        let fixture: ComponentFixture<MetersUpdateComponent>;
        let service: MetersService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WebillTestModule],
                declarations: [MetersUpdateComponent]
            })
                .overrideTemplate(MetersUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(MetersUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MetersService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Meters(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.meters = entity;
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
                    const entity = new Meters();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.meters = entity;
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
