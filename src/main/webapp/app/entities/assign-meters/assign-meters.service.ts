import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAssignMeters } from 'app/shared/model/assign-meters.model';
import {IUser} from "app/core";

type EntityResponseType = HttpResponse<IAssignMeters>;
type EntityArrayResponseType = HttpResponse<IAssignMeters[]>;
type EntityConsumerResponseType = HttpResponse<IUser[]>;

@Injectable({ providedIn: 'root' })
export class AssignMetersService {
    private resourceUrl = SERVER_API_URL + 'api/assign-meters';

    constructor(private http: HttpClient) {}

    create(assignMeters: IAssignMeters): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(assignMeters);
        return this.http
            .post<IAssignMeters>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(assignMeters: IAssignMeters): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(assignMeters);
        return this.http
            .put<IAssignMeters>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IAssignMeters>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAssignMeters[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    mymeters(): Observable<EntityResponseType> {
        return this.http.get<IAssignMeters>('api/my-meters', { observe: 'response' });
    }

    // consumers(): Observable<EntityConsumerResponseType> {
    //     return this.http.get<IUser[]>(SERVER_API_URL + 'api/users-consumers',{ observe: 'response' });
    // }



    private convertDateFromClient(assignMeters: IAssignMeters): IAssignMeters {
        const copy: IAssignMeters = Object.assign({}, assignMeters, {
            createdAt: assignMeters.createdAt != null && assignMeters.createdAt.isValid() ? assignMeters.createdAt.toJSON() : null,
            updateAt: assignMeters.updateAt != null && assignMeters.updateAt.isValid() ? assignMeters.updateAt.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.createdAt = res.body.createdAt != null ? moment(res.body.createdAt) : null;
        res.body.updateAt = res.body.updateAt != null ? moment(res.body.updateAt) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((assignMeters: IAssignMeters) => {
            assignMeters.createdAt = assignMeters.createdAt != null ? moment(assignMeters.createdAt) : null;
            assignMeters.updateAt = assignMeters.updateAt != null ? moment(assignMeters.updateAt) : null;
        });
        return res;
    }
}
