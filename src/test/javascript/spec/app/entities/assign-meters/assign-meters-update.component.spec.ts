/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { WebillTestModule } from '../../../test.module';
import { AssignMetersUpdateComponent } from 'app/entities/assign-meters/assign-meters-update.component';
import { AssignMetersService } from 'app/entities/assign-meters/assign-meters.service';
import { AssignMeters } from 'app/shared/model/assign-meters.model';

describe('Component Tests', () => {
    describe('AssignMeters Management Update Component', () => {
        let comp: AssignMetersUpdateComponent;
        let fixture: ComponentFixture<AssignMetersUpdateComponent>;
        let service: AssignMetersService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WebillTestModule],
                declarations: [AssignMetersUpdateComponent]
            })
                .overrideTemplate(AssignMetersUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(AssignMetersUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AssignMetersService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new AssignMeters(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.assignMeters = entity;
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
                    const entity = new AssignMeters();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.assignMeters = entity;
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
