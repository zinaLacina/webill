/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { SettingService } from 'app/entities/setting/setting.service';
import { ISetting, Setting } from 'app/shared/model/setting.model';

describe('Service Tests', () => {
    describe('Setting Service', () => {
        let injector: TestBed;
        let service: SettingService;
        let httpMock: HttpTestingController;
        let elemDefault: ISetting;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(SettingService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Setting(null);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        dateCreated: currentDate.format(DATE_TIME_FORMAT),
                        dateModified: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a Setting', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        dateCreated: currentDate.format(DATE_TIME_FORMAT),
                        dateModified: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        dateCreated: currentDate,
                        dateModified: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(null,null)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Setting', async () => {
                const returnedFromService = Object.assign(
                    {
                        companyName: 'BBBBBB',
                        companyLogo: 'BBBBBB',
                        companyNumber: 'BBBBBB',
                        companyAddress: 'BBBBBB',
                        dateCreated: currentDate.format(DATE_TIME_FORMAT),
                        dateModified: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        dateCreated: currentDate,
                        dateModified: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(null,null)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of Setting', async () => {
                const returnedFromService = Object.assign(
                    {
                        companyName: 'BBBBBB',
                        companyLogo: 'BBBBBB',
                        companyNumber: 'BBBBBB',
                        companyAddress: 'BBBBBB',
                        dateCreated: currentDate.format(DATE_TIME_FORMAT),
                        dateModified: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        dateCreated: currentDate,
                        dateModified: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a Setting', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
