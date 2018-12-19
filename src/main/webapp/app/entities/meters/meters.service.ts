import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMeters } from 'app/shared/model/meters.model';

type EntityResponseType = HttpResponse<IMeters>;
type EntityArrayResponseType = HttpResponse<IMeters[]>;

@Injectable({ providedIn: 'root' })
export class MetersService {
    private resourceUrl = SERVER_API_URL + 'api/meters';

    constructor(private http: HttpClient) {}

    create(meters: IMeters): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(meters);
        return this.http
            .post<IMeters>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(meters: IMeters): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(meters);
        return this.http
            .put<IMeters>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IMeters>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMeters[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    public convertDateFromClient(meters: IMeters): IMeters {
        const copy: IMeters = Object.assign({}, meters, {
            dateCreated: meters.dateCreated != null && meters.dateCreated.isValid() ? meters.dateCreated.toJSON() : null,
            dateModified: meters.dateModified != null && meters.dateModified.isValid() ? meters.dateModified.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.dateCreated = res.body.dateCreated != null ? moment(res.body.dateCreated) : null;
        res.body.dateModified = res.body.dateModified != null ? moment(res.body.dateModified) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((meters: IMeters) => {
            meters.dateCreated = meters.dateCreated != null ? moment(meters.dateCreated) : null;
            meters.dateModified = meters.dateModified != null ? moment(meters.dateModified) : null;
        });
        return res;
    }
}
