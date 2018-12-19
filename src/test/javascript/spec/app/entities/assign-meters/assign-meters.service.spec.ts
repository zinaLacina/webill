/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { AssignMetersService } from 'app/entities/assign-meters/assign-meters.service';
import { IAssignMeters, AssignMeters } from 'app/shared/model/assign-meters.model';

describe('Service Tests', () => {
    describe('AssignMeters Service', () => {
        let injector: TestBed;
        let service: AssignMetersService;
        let httpMock: HttpTestingController;
        let elemDefault: IAssignMeters;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(AssignMetersService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new AssignMeters(null);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        createdAt: currentDate.format(DATE_TIME_FORMAT),
                        updateAt: currentDate.format(DATE_TIME_FORMAT)
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

            it('should create a AssignMeters', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        createdAt: currentDate.format(DATE_TIME_FORMAT),
                        updateAt: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        createdAt: currentDate,
                        updateAt: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new AssignMeters(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a AssignMeters', async () => {
                const returnedFromService = Object.assign(
                    {
                        startMonth: 1,
                        endMonth: 1,
                        startDay: 1,
                        endDay: 1,
                        year: 1,
                        reasonEnd: 'BBBBBB',
                        enabled: true,
                        createdAt: currentDate.format(DATE_TIME_FORMAT),
                        updateAt: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        createdAt: currentDate,
                        updateAt: currentDate
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

            it('should return a list of AssignMeters', async () => {
                const returnedFromService = Object.assign(
                    {
                        startMonth: 1,
                        endMonth: 1,
                        startDay: 1,
                        endDay: 1,
                        year: 1,
                        reasonEnd: 'BBBBBB',
                        enabled: true,
                        createdAt: currentDate.format(DATE_TIME_FORMAT),
                        updateAt: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        createdAt: currentDate,
                        updateAt: currentDate
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

            it('should delete a AssignMeters', async () => {
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
