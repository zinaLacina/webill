/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WebillTestModule } from '../../../test.module';
import { SettingDetailComponent } from 'app/entities/setting/setting-detail.component';
import { Setting } from 'app/shared/model/setting.model';

describe('Component Tests', () => {
    describe('Setting Management Detail Component', () => {
        let comp: SettingDetailComponent;
        let fixture: ComponentFixture<SettingDetailComponent>;
        const route = ({ data: of({ setting: new Setting(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WebillTestModule],
                declarations: [SettingDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SettingDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SettingDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.setting).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
