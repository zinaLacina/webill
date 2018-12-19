/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { NotificationsService } from 'app/entities/notifications/notifications.service';
import { INotifications, Notifications } from 'app/shared/model/notifications.model';

describe('Service Tests', () => {
    describe('Notifications Service', () => {
        let injector: TestBed;
        let service: NotificationsService;
        let httpMock: HttpTestingController;
        let elemDefault: INotifications;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(NotificationsService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Notifications(0, 'AAAAAAA', false, currentDate, currentDate);
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

            it('should create a Notifications', async () => {
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
                    .create(new Notifications(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Notifications', async () => {
                const returnedFromService = Object.assign(
                    {
                        message: 'BBBBBB',
                        checked: true,
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

            it('should return a list of Notifications', async () => {
                const returnedFromService = Object.assign(
                    {
                        message: 'BBBBBB',
                        checked: true,
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

            it('should delete a Notifications', async () => {
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
