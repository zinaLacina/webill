import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { INotifications } from 'app/shared/model/notifications.model';

type EntityResponseType = HttpResponse<INotifications>;
type EntityArrayResponseType = HttpResponse<INotifications[]>;

@Injectable({ providedIn: 'root' })
export class NotificationsService {
    private resourceUrl = SERVER_API_URL + 'api/notifications';

    constructor(private http: HttpClient) {}

    create(notifications: INotifications): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(notifications);
        return this.http
            .post<INotifications>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(notifications: INotifications): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(notifications);
        return this.http
            .put<INotifications>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<INotifications>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<INotifications[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    checked(id:number){
        return this.http
            .get<INotifications>(`${this.resourceUrl}/checked/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(notifications: INotifications): INotifications {
        const copy: INotifications = Object.assign({}, notifications, {
            createdAt: notifications.createdAt != null && notifications.createdAt.isValid() ? notifications.createdAt.toJSON() : null,
            updateAt: notifications.updateAt != null && notifications.updateAt.isValid() ? notifications.updateAt.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.createdAt = res.body.createdAt != null ? moment(res.body.createdAt) : null;
        res.body.updateAt = res.body.updateAt != null ? moment(res.body.updateAt) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((notifications: INotifications) => {
            notifications.createdAt = notifications.createdAt != null ? moment(notifications.createdAt) : null;
            notifications.updateAt = notifications.updateAt != null ? moment(notifications.updateAt) : null;
        });
        return res;
    }
}
