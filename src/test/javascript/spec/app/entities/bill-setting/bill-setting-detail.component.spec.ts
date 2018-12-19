/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WebillTestModule } from '../../../test.module';
import { BillSettingDetailComponent } from 'app/entities/bill-setting/bill-setting-detail.component';
import { BillSetting } from 'app/shared/model/bill-setting.model';

describe('Component Tests', () => {
    describe('BillSetting Management Detail Component', () => {
        let comp: BillSettingDetailComponent;
        let fixture: ComponentFixture<BillSettingDetailComponent>;
        const route = ({ data: of({ billSetting: new BillSetting(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WebillTestModule],
                declarations: [BillSettingDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(BillSettingDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BillSettingDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.billSetting).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
