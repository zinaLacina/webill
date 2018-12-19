/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { WebillTestModule } from '../../../test.module';
import { SettingUpdateComponent } from 'app/entities/setting/setting-update.component';
import { SettingService } from 'app/entities/setting/setting.service';
import { Setting } from 'app/shared/model/setting.model';

describe('Component Tests', () => {
    describe('Setting Management Update Component', () => {
        let comp: SettingUpdateComponent;
        let fixture: ComponentFixture<SettingUpdateComponent>;
        let service: SettingService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WebillTestModule],
                declarations: [SettingUpdateComponent]
            })
                .overrideTemplate(SettingUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SettingUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SettingService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Setting(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.setting = entity;
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
                    const entity = new Setting();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.setting = entity;
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
