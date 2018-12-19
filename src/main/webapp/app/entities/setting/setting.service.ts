import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISetting } from 'app/shared/model/setting.model';

type EntityResponseType = HttpResponse<ISetting>;
type EntityArrayResponseType = HttpResponse<ISetting[]>;

@Injectable({ providedIn: 'root' })
export class SettingService {
    private resourceUrl = SERVER_API_URL + 'api/settings';

    constructor(private http: HttpClient) {}

    create(setting: ISetting,file: File): Observable<EntityResponseType> {
        const formdata: FormData = new FormData();
        //const copy = this.convertDateFromClient(setting);
        formdata.append('file', file);
        formdata.append("companyName",setting.companyName);
        formdata.append("companyAddress",setting.companyAddress);
        formdata.append("companyNumber",setting.companyNumber);

        //console.log(setting.id);

        if(typeof setting.id !== 'undefined' && setting.id !=null){
            formdata.append("id",""+setting.id);
        }else{
            formdata.append("id","");
        }
        //console.log(formdata);

        return this.http
            .post<ISetting>(this.resourceUrl, formdata, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(setting: ISetting,file: File): Observable<EntityResponseType> {
        // const copy = this.convertDateFromClient(setting);
        const formdata: FormData = new FormData();
        //const copy = this.convertDateFromClient(setting);
        formdata.append('file', file);
        formdata.append("companyName",setting.companyName);
        formdata.append("companyAddress",setting.companyAddress);
        formdata.append("companyNumber",setting.companyNumber);

        //console.log(setting.id);

        if(typeof setting.id !== 'undefined' && setting.id !=null){
            formdata.append("id",""+setting.id);
        }else{
            formdata.append("id","");
        }
        return this.http
            .put<ISetting>(this.resourceUrl, formdata, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ISetting>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISetting[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(setting: ISetting): ISetting {
        const copy: ISetting = Object.assign({}, setting, {
            dateCreated: setting.dateCreated != null && setting.dateCreated.isValid() ? setting.dateCreated.toJSON() : null,
            dateModified: setting.dateModified != null && setting.dateModified.isValid() ? setting.dateModified.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.dateCreated = res.body.dateCreated != null ? moment(res.body.dateCreated) : null;
        res.body.dateModified = res.body.dateModified != null ? moment(res.body.dateModified) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((setting: ISetting) => {
            setting.dateCreated = setting.dateCreated != null ? moment(setting.dateCreated) : null;
            setting.dateModified = setting.dateModified != null ? moment(setting.dateModified) : null;
        });
        return res;
    }
}
