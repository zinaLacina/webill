import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBillSetting } from 'app/shared/model/bill-setting.model';

type EntityResponseType = HttpResponse<IBillSetting>;
type EntityArrayResponseType = HttpResponse<IBillSetting[]>;

@Injectable({ providedIn: 'root' })
export class BillSettingService {
    private resourceUrl = SERVER_API_URL + 'api/bill-settings';

    constructor(private http: HttpClient) {}

    create(billSetting: IBillSetting): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(billSetting);
        return this.http
            .post<IBillSetting>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(billSetting: IBillSetting): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(billSetting);
        return this.http
            .put<IBillSetting>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IBillSetting>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IBillSetting[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(billSetting: IBillSetting): IBillSetting {
        const copy: IBillSetting = Object.assign({}, billSetting, {
            dateCreated: billSetting.dateCreated != null && billSetting.dateCreated.isValid() ? billSetting.dateCreated.toJSON() : null,
            dateModified: billSetting.dateModified != null && billSetting.dateModified.isValid() ? billSetting.dateModified.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.dateCreated = res.body.dateCreated != null ? moment(res.body.dateCreated) : null;
        res.body.dateModified = res.body.dateModified != null ? moment(res.body.dateModified) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((billSetting: IBillSetting) => {
            billSetting.dateCreated = billSetting.dateCreated != null ? moment(billSetting.dateCreated) : null;
            billSetting.dateModified = billSetting.dateModified != null ? moment(billSetting.dateModified) : null;
        });
        return res;
    }
}
