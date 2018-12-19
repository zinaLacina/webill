/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WebillTestModule } from '../../../test.module';
import { AssignMetersDetailComponent } from 'app/entities/assign-meters/assign-meters-detail.component';
import { AssignMeters } from 'app/shared/model/assign-meters.model';

describe('Component Tests', () => {
    describe('AssignMeters Management Detail Component', () => {
        let comp: AssignMetersDetailComponent;
        let fixture: ComponentFixture<AssignMetersDetailComponent>;
        const route = ({ data: of({ assignMeters: new AssignMeters(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WebillTestModule],
                declarations: [AssignMetersDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(AssignMetersDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AssignMetersDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.assignMeters).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
