import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IAssignMeters } from 'app/shared/model//assign-meters.model';
import { IBillSetting } from 'app/shared/model//bill-setting.model';

export interface IBills {
    id?: number;
    lastMonthReading?: number;
    currentMonthReading?: number;
    deadline?: Moment;
    uploadedContent?: string;
    billFile?: any;
    paid?: boolean;
    notRejected?: boolean;
    imageFile?: any;
    billCode?: string;
    invoiceN?: number;
    amount?: number;
    enabled?: boolean;
    dateCreated?: Moment;
    dateModified?: Moment;
    ownerUser?: IUser;
    verifierUser?: IUser;
    verifierMetricBill?: IAssignMeters;
    billSettingApp?: IBillSetting;
}

export class Bills implements IBills {
    constructor(
        public id?: number,
        public lastMonthReading?: number,
        public currentMonthReading?: number,
        public deadline?: Moment,
        public uploadedContent?: string,
        public billFile?: any,
        public paid?: boolean,
        public notRejected?: boolean,
        public imageFile?: any,
        public billCode?: string,
        public invoiceN?: number,
        public amount?: number,
        public enabled?: boolean,
        public dateCreated?: Moment,
        public dateModified?: Moment,
        public ownerUser?: IUser,
        public verifierUser?: IUser,
        public verifierMetricBill?: IAssignMeters,
        public billSettingApp?: IBillSetting
    ) {
        this.enabled = this.enabled || false;
    }
}
