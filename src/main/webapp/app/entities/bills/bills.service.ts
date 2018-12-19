import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBills } from 'app/shared/model/bills.model';

type EntityResponseType = HttpResponse<IBills>;
type EntityArrayResponseType = HttpResponse<IBills[]>;

@Injectable({ providedIn: 'root' })
export class BillsService {
    private resourceUrl = SERVER_API_URL + 'api/bills';
    private  ressourceUrlAccept = SERVER_API_URL + 'api/bills/activate';

    constructor(private http: HttpClient) {}

    create(bills: IBills): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(bills);
        return this.http
            .post<IBills>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(bills: IBills): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(bills);
        return this.http
            .put<IBills>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }
    accept(bills: IBills): Observable<EntityResponseType> {
        const formdata: FormData = new FormData();
        formdata.append("billId",bills.id+"");
        if(bills.enabled){
            formdata.append("accepted","1");
        }else{
            formdata.append("accepted","0");
        }

        return this.http
            .post<IBills>(this.ressourceUrlAccept, formdata, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IBills>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
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

    private convertDateFromClient(bills: IBills): IBills {
        const copy: IBills = Object.assign({}, bills, {
            deadline: bills.deadline != null && bills.deadline.isValid() ? bills.deadline.toJSON() : null,
            dateCreated: bills.dateCreated != null && bills.dateCreated.isValid() ? bills.dateCreated.toJSON() : null,
            dateModified: bills.dateModified != null && bills.dateModified.isValid() ? bills.dateModified.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.deadline = res.body.deadline != null ? moment(res.body.deadline) : null;
        res.body.dateCreated = res.body.dateCreated != null ? moment(res.body.dateCreated) : null;
        res.body.dateModified = res.body.dateModified != null ? moment(res.body.dateModified) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((bills: IBills) => {
            bills.deadline = bills.deadline != null ? moment(bills.deadline) : null;
            bills.dateCreated = bills.dateCreated != null ? moment(bills.dateCreated) : null;
            bills.dateModified = bills.dateModified != null ? moment(bills.dateModified) : null;
        });
        return res;
    }
}
