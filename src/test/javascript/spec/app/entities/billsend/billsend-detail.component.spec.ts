/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WebillTestModule } from '../../../test.module';
import { BillsendDetailComponent } from 'app/entities/billsend/billsend-detail.component';
import { Billsend } from 'app/shared/model/billsend.model';

describe('Component Tests', () => {
    describe('Billsend Management Detail Component', () => {
        let comp: BillsendDetailComponent;
        let fixture: ComponentFixture<BillsendDetailComponent>;
        const route = ({ data: of({ billsend: new Billsend(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WebillTestModule],
                declarations: [BillsendDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(BillsendDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BillsendDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.billsend).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
