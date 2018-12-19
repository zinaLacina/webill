/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { MetersService } from 'app/entities/meters/meters.service';
import { IMeters, Meters } from 'app/shared/model/meters.model';

describe('Service Tests', () => {
    describe('Meters Service', () => {
        let injector: TestBed;
        let service: MetersService;
        let httpMock: HttpTestingController;
        let elemDefault: IMeters;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(MetersService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Meters(null);
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

            it('should create a Meters', async () => {
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
                    .create(new Meters(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Meters', async () => {
                const returnedFromService = Object.assign(
                    {
                        latituude: 1,
                        longitude: 1,
                        qrCode: 'BBBBBB',
                        addressMeters: 'BBBBBB',
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

            it('should return a list of Meters', async () => {
                const returnedFromService = Object.assign(
                    {
                        latituude: 1,
                        longitude: 1,
                        qrCode: 'BBBBBB',
                        addressMeters: 'BBBBBB',
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

            it('should delete a Meters', async () => {
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
