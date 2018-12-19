/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { BillSettingService } from 'app/entities/bill-setting/bill-setting.service';
import { IBillSetting, BillSetting, TypeBill } from 'app/shared/model/bill-setting.model';

describe('Service Tests', () => {
    describe('BillSetting Service', () => {
        let injector: TestBed;
        let service: BillSettingService;
        let httpMock: HttpTestingController;
        let elemDefault: IBillSetting;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(BillSettingService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new BillSetting(0, TypeBill.COMPANY, 0, 0, 0, 0, false, currentDate, currentDate);
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

            it('should create a BillSetting', async () => {
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
                    .create(new BillSetting(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a BillSetting', async () => {
                const returnedFromService = Object.assign(
                    {
                        typeBill: 'BBBBBB',
                        pricePerKW: 1,
                        discount: 1,
                        tax: 1,
                        processing: 1,
                        enabled: true,
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
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of BillSetting', async () => {
                const returnedFromService = Object.assign(
                    {
                        typeBill: 'BBBBBB',
                        pricePerKW: 1,
                        discount: 1,
                        tax: 1,
                        processing: 1,
                        enabled: true,
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

            it('should delete a BillSetting', async () => {
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
