import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBillsend } from 'app/shared/model/billsend.model';
import {IBills} from "app/shared/model/bills.model";

type EntityResponseType = HttpResponse<IBillsend>;
type EntityArrayResponseType = HttpResponse<IBillsend[]>;

@Injectable({ providedIn: 'root' })
export class BillsendService {
    private resourceUrl = SERVER_API_URL + 'api/bills';
    private resourceUrlUpload = SERVER_API_URL + 'api/billsends';
    private resourceMyBills = SERVER_API_URL+"api/my-bills";

    constructor(private http: HttpClient) {}

    create(billsend: IBillsend,file: File): Observable<EntityResponseType> {
        //const copy = this.convertDateFromClient(billsend);
        const formdata: FormData = new FormData();
        //const copy = this.convertDateFromClient(setting);
        formdata.append('file', file);
        return this.http
            .post<IBillsend>(this.resourceUrlUpload, formdata, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(billsend: IBillsend,file: File): Observable<EntityResponseType> {
        //const copy = this.convertDateFromClient(billsend);
        const formdata: FormData = new FormData();
        //const copy = this.convertDateFromClient(setting);
        formdata.append('file', file);
        return this.http
            .put<IBillsend>(this.resourceUrlUpload, formdata, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IBills>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    findMyBills(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IBills[]>(this.resourceMyBills, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IBills[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(billsend: IBillsend): IBillsend {
        const copy: IBillsend = Object.assign({}, billsend, {
            dateCreated: billsend.dateCreated != null && billsend.dateCreated.isValid() ? billsend.dateCreated.toJSON() : null,
            dateModified: billsend.dateModified != null && billsend.dateModified.isValid() ? billsend.dateModified.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.dateCreated = res.body.dateCreated != null ? moment(res.body.dateCreated) : null;
        res.body.dateModified = res.body.dateModified != null ? moment(res.body.dateModified) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((billsend: IBillsend) => {
            billsend.dateCreated = billsend.dateCreated != null ? moment(billsend.dateCreated) : null;
            billsend.dateModified = billsend.dateModified != null ? moment(billsend.dateModified) : null;
        });
        return res;
    }
}
