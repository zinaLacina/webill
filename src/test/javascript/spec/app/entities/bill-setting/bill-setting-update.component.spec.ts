/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { WebillTestModule } from '../../../test.module';
import { BillSettingUpdateComponent } from 'app/entities/bill-setting/bill-setting-update.component';
import { BillSettingService } from 'app/entities/bill-setting/bill-setting.service';
import { BillSetting } from 'app/shared/model/bill-setting.model';

describe('Component Tests', () => {
    describe('BillSetting Management Update Component', () => {
        let comp: BillSettingUpdateComponent;
        let fixture: ComponentFixture<BillSettingUpdateComponent>;
        let service: BillSettingService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WebillTestModule],
                declarations: [BillSettingUpdateComponent]
            })
                .overrideTemplate(BillSettingUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(BillSettingUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BillSettingService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new BillSetting(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.billSetting = entity;
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
                    const entity = new BillSetting();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.billSetting = entity;
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
