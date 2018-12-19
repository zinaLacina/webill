/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WebillTestModule } from '../../../test.module';
import { MetersDetailComponent } from 'app/entities/meters/meters-detail.component';
import { Meters } from 'app/shared/model/meters.model';

describe('Component Tests', () => {
    describe('Meters Management Detail Component', () => {
        let comp: MetersDetailComponent;
        let fixture: ComponentFixture<MetersDetailComponent>;
        const route = ({ data: of({ meters: new Meters(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WebillTestModule],
                declarations: [MetersDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(MetersDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(MetersDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.meters).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
